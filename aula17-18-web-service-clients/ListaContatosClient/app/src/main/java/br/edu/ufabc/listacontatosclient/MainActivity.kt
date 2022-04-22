package br.edu.ufabc.listacontatosclient

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.edu.ufabc.listacontatosclient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(getNavController())

        bindEvents()
    }

    private fun bindEvents() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressLinear.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun getNavHost() = supportFragmentManager.findFragmentById(R.id.fragment_container)
            as NavHostFragment

    private fun getNavController() = getNavHost().navController
}