package br.edu.ufabc.todoasync

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.todoasync.databinding.ActivityMainBinding
import br.edu.ufabc.todoasync.view.FilterCriteria
import br.edu.ufabc.todoasync.view.ListFragmentDirections
import br.edu.ufabc.todoasync.viewmodel.MainViewModel
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        initComponents()
        configureStaticMenu()
        viewModel.isDataReady.observe(this) { isReady ->
            if (isReady) {
                configureDynamicMenu()
                binding.progressIndicator.visibility = View.INVISIBLE
            } else {
                binding.progressIndicator.visibility = View.VISIBLE
            }
        }
    }

    private fun initComponents() {
        appBarConfiguration = AppBarConfiguration(setOf(R.id.destination_task_list), binding.drawerLayout)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(getNavController(), appBarConfiguration)
        binding.navigationView.setupWithNavController(getNavController())
    }

    private fun configureStaticMenu() {
        binding.navigationView.menu.forEach {
            it.setOnMenuItemClickListener { menuItem ->
                val action = when (menuItem?.itemId) {
                    R.id.all_tasks -> ListFragmentDirections.filterTaskList(
                        filterCriteria = FilterCriteria.ALL,
                        title = getString(R.string.all_tasks_title))
                    R.id.overdue_tasks -> ListFragmentDirections.filterTaskList(
                        filterCriteria = FilterCriteria.OVERDUE,
                        title = getString(R.string.overdue_tasks_title))
                    R.id.completed_tasks -> ListFragmentDirections.filterTaskList(
                        filterCriteria = FilterCriteria.COMPLETED,
                        title = getString(R.string.completed_tasks_title))
                    else -> ListFragmentDirections.filterTaskList()
                }
                binding.drawerLayout.close()
                getNavController().navigate(action)
                true
            }
        }
    }

    private fun configureDynamicMenu() {
        binding.navigationView.menu.findItem(R.id.tags_menu)?.let { menuItem ->
            menuItem.subMenu?.let { subMenu ->
                viewModel.getTags().sorted().forEach { theTag ->
                    subMenu.add(theTag).setOnMenuItemClickListener {
                        val action = ListFragmentDirections.filterTaskList(
                            filterCriteria = FilterCriteria.TAG,
                            title = getString(R.string.tag_title, theTag),
                            tag = theTag)
                        binding.drawerLayout.close()
                        getNavController().navigate(action)
                        true
                    }
                }
            } ?: throw Exception("Failed to find tags submenu")
        } ?: throw Exception("Failed to find tags menu item")
    }

    private fun getNavController() =
        (supportFragmentManager.findFragmentById(binding.navHostFragmentContentMain.id) as NavHostFragment).navController


    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}