package br.edu.ufabc.flickrgallery

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.flickrgallery.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(getNavController())
    }

    override fun onStart() {
        super.onStart()

        viewModel.getRecentPhotos(null).observe(this) { result ->
            when (result.status) {
                is MainViewModel.Status.Success -> {

                }
                is MainViewModel.Status.Error -> {
                    Log.e("VIEW", "Failed to list items", result.status.e)
                    Log.e("", result.status.e.stackTraceToString())
                    Snackbar.make(binding.root, "No data to display", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getNavHostFragment() = supportFragmentManager.findFragmentById(R.id.fragment_container)
            as NavHostFragment

    private fun getNavController() = getNavHostFragment().navController

    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp() || super.onSupportNavigateUp()
    }
}