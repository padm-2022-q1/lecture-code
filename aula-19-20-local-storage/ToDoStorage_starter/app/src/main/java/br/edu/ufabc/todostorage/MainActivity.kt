package br.edu.ufabc.todostorage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.todostorage.databinding.ActivityMainBinding
import br.edu.ufabc.todostorage.view.FilterCriteria
import br.edu.ufabc.todostorage.view.ListFragmentDirections
import br.edu.ufabc.todostorage.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Main activity.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        configureStaticMenu()
        bindEvents()
    }

    private fun initComponents() {
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.destination_list), binding.drawerLayout)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(getNavController(), appBarConfiguration)
        binding.navigationView.setupWithNavController(getNavController())
    }

    private fun configureStaticMenu() = try {
        binding.navigationView.menu.forEach {
            it.setOnMenuItemClickListener { menuItem ->
                val action = when (menuItem?.itemId) {
                    R.id.all_tasks -> ListFragmentDirections.filterTaskList(
                        filterCriteria = FilterCriteria.ALL,
                        title = getString(R.string.all_tasks_title)
                    )
                    R.id.overdue_tasks -> ListFragmentDirections.filterTaskList(
                        filterCriteria = FilterCriteria.OVERDUE,
                        title = getString(R.string.overdue_tasks_title)
                    )
                    R.id.completed_tasks -> ListFragmentDirections.filterTaskList(
                        filterCriteria = FilterCriteria.COMPLETED,
                        title = getString(R.string.completed_tasks_title)
                    )
                    else -> ListFragmentDirections.filterTaskList()
                }
                binding.drawerLayout.close()
                getNavController().navigate(action)
                true
            }
        }
    } catch (e: Exception) {
        Log.e("VIEW", "Failed to configure static menu", e)
        notifyError("Failed to render filter menu")
    }

    private fun loadDynamicMenu() {
        binding.navigationView.menu.findItem(R.id.tags_menu)?.subMenu?.let { subMenu ->
            subMenu.clear()
            viewModel.getTags().observe(this) { status ->
                when (status) {
                    is MainViewModel.Status.Success -> {
                        val tags = (status.result as MainViewModel.Result.TagList).value

                        tags.sorted().forEach { theTag ->
                            subMenu.add(theTag).setOnMenuItemClickListener {
                                ListFragmentDirections.filterTaskList(
                                    filterCriteria = FilterCriteria.TAG,
                                    title = getString(R.string.tag_title, theTag),
                                    tag = theTag
                                ).let {
                                    binding.drawerLayout.close()
                                    getNavController().navigate(it)
                                }
                                true
                            }
                        }
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to list tags", status.e)
                        notifyError("Failed to render tags menu")
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                    is MainViewModel.Status.Loading -> {
                        binding.progressHorizontal.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun bindEvents() {
        viewModel.shouldRefresh.observe(this) {
            if (it) {
                loadDynamicMenu()
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getNavController(): NavController {
        val fragment = supportFragmentManager.findFragmentById(binding.navHostFragmentContentMain.id)

        return (fragment as NavHostFragment).navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
