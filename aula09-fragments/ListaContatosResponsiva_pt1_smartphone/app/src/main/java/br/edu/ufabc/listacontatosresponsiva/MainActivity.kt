package br.edu.ufabc.listacontatosresponsiva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import br.edu.ufabc.listacontatosresponsiva.databinding.ActivityMainBinding
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    companion object {
        private var contactsFile = "contacts.json"
    }
    private lateinit var contacts: List<Contact>
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        doLayout()
        bindEvents()
    }

    private fun loadData() {
        try {
            resources.assets.open(contactsFile).use {
                contacts = Klaxon().parseArray(it) ?: emptyList()
            }
        } catch (e: FileNotFoundException) {
            Log.e("APP", "Failed to open dataset file", e)
            Snackbar.make(binding.root.rootView,
                "Failed to access contacts data", Snackbar.LENGTH_INDEFINITE).show()
        } catch (e: KlaxonException) {
            Log.e("APP", "Failed to parse dataset file", e)
            Snackbar.make(binding.root.rootView,
                "Failed to read contacts data", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun doLayout() {
        val mainContainer = binding.mainFragmentContainer

        supportFragmentManager.commit {
            replace(mainContainer.id, ContactListFragment(contacts))
        }
    }

    private fun bindEvents() {
        supportFragmentManager.setFragmentResultListener(ContactListFragment.itemClickedKey,
            this) { _, bundle ->
            val position = bundle.getInt(ContactListFragment.itemClickedPosition)
            val contact = contacts[position]

            supportFragmentManager.commit {
                replace(binding.mainFragmentContainer.id, ContactItemFragment(contact))
                addToBackStack(null)
            }
        }
    }
}