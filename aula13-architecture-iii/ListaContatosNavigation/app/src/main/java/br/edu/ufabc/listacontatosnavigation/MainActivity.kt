package br.edu.ufabc.listacontatosnavigation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import br.edu.ufabc.listacontatosnavigation.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private class CustomOnBackPressedCallback(
        private val slidingPaneLayout: SlidingPaneLayout,
        private val viewModel: MainViewModel) :
        OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen),
        SlidingPaneLayout.PanelSlideListener {

        init {
            slidingPaneLayout.addPanelSlideListener(this)
        }

        override fun handleOnBackPressed() {
            slidingPaneLayout.closePane()
            viewModel.clickedItemId.value = null
        }

        override fun onPanelSlide(panel: View, slideOffset: Float) {}

        override fun onPanelOpened(panel: View) {
            isEnabled = true
        }

        override fun onPanelClosed(panel: View) {
            isEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindEvents()
    }

    private fun bindEvents() {
        try {
            onBackPressedDispatcher.addCallback(this@MainActivity,
                CustomOnBackPressedCallback(binding.slidingPaneLayout, viewModel))
            viewModel.clickedItemId.observe(this) {
                it?.let { contactId ->
                    val navController = binding.contactItemFragmentContainer.findNavController()
                    val action = ContactItemFragmentDirections.showItemDetails(contactId)

                    navController.navigate(action, navOptions {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    })
                    binding.slidingPaneLayout.open()
                }
            }
        } catch (e: IllegalStateException) {
            Log.e("APP", e.message, e)
            Snackbar.make(binding.root.rootView, "Failed to build app layout",
                Snackbar.LENGTH_INDEFINITE).show()
        }
    }

}