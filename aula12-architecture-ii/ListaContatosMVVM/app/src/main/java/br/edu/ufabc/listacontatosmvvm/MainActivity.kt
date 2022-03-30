package br.edu.ufabc.listacontatosmvvm

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import br.edu.ufabc.listacontatosmvvm.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindEvents()
    }

    private fun isTablet() = resources.configuration.smallestScreenWidthDp >= 600

    private fun isLandscape() =
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    private fun bindEvents() {
        try {
            if (isTablet() || isLandscape()) bindTwoPaneEvents() else bindOnePaneEvents()
        } catch (e: IllegalStateException) {
            Log.e("APP", e.message, e)
            Snackbar.make(binding.root.rootView, "Failed to build app layout",
                Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun bindOnePaneEvents() {
        viewModel.clickedItemId.observe(this) {
            it?.let {
                supportFragmentManager.commit {
                    ContactItemFragment().apply {
                        binding.mainFragmentContainer?.let { container ->
                            replace(container.id, this)
                            addToBackStack(null)
                        }
                    }
                }
            }
        }
    }

    private fun bindTwoPaneEvents() {
        viewModel.clickedItemId.observe(this) {
            it?.let {
                binding.contactItemFragmentContainer?.let { container ->
                    supportFragmentManager.commit {
                        ContactItemFragment().apply {
                            replace(container.id, this)
                        }
                    }
                }
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        viewModel.clickedItemId.value = null
    }

}