package br.edu.ufabc.listacontatosresponsiva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import br.edu.ufabc.listacontatosresponsiva.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindEvents()
    }

    private fun isTablet() = resources.configuration.smallestScreenWidthDp >= 600

    private fun bindEvents() {
        try {
            if (isTablet()) bindTwoPaneEvents() else bindOnePaneEvents()
        } catch (e: IllegalStateException) {
            Log.e("APP", e.message, e)
            Snackbar.make(binding.root.rootView, "Failed to build app layout",
                Snackbar.LENGTH_INDEFINITE).show()
        }

    }

    private fun bindOnePaneEvents() {
        supportFragmentManager.setFragmentResultListener(ContactListFragment.itemClickedKey,
            this) { _, bundle ->
            val position = bundle.getInt(ContactListFragment.itemClickedPosition)

            supportFragmentManager.commit {
                ContactItemFragment().apply {
                    arguments = bundleOf(ContactItemFragment.contactPosition to position)
                    replace(binding.contactListFragmentContainer.id, this)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun bindTwoPaneEvents() {
        supportFragmentManager.setFragmentResultListener(ContactListFragment.itemClickedKey,
            this) { _, bundle ->
            val position = bundle.getInt(ContactListFragment.itemClickedPosition)

            binding.contactItemFragmentContainer?.let { container ->
                supportFragmentManager.commit {
                    ContactItemFragment().apply {
                        arguments = bundleOf(ContactItemFragment.contactPosition to position)
                        replace(container.id, this)
                    }
                }
            }

        }
    }

}