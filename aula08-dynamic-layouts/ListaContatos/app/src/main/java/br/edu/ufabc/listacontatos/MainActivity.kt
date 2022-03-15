package br.edu.ufabc.listacontatos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.listacontatos.databinding.ActivityMainBinding
import br.edu.ufabc.listacontatos.databinding.ContactListItemBinding
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException

/**
 * Main activity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Static variables.
     */
    companion object {
        private const val contactsFile = "contacts.json"
    }

    /**
     * Transfer (domain) object.
     */
    private data class Contact(
        @Json(name = "name")
        var name: String,
        var email: String,
        var phone: String,
        var address: String
    )

    /**
     * Adapter.
     */
    private class ContactAdapter(val contacts: List<Contact>) :
        RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

        /**
         * View Holder.
         */
        private class ContactHolder(itemBinding: ContactListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val name = itemBinding.contactListItemFullname
            val email = itemBinding.contactListItemEmailValue
            val phone = itemBinding.contactListItemPhoneValue
            val address = itemBinding.contactListItemAddressValue
        }

        /**
         * Create a view holder.
         */
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ContactHolder =
            ContactHolder(
                ContactListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        /**
         * Populate a view holder with data.
         */
        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            val contact = contacts[position]

            holder.name.text = contact.name
            holder.email.text = contact.email
            holder.phone.text = contact.phone
            holder.address.text = contact.address
        }

        /**
         * The total quantity of items in the list.
         */
        override fun getItemCount(): Int = contacts.size

        /**
         * Called when a view holder is recycled.
         */
        override fun onViewRecycled(holder: ContactHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.adapterPosition}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        try {
            /**
             * Open the JSON file in the assets folder.
             */
            resources.assets.open(contactsFile).use {
                binding.recyclerviewContactList.apply {
                    /**
                     * - Deserialize the JSON file.
                     * - Inject the resulting list in the adapter.
                     * - Bind the adapter to the RecyclerView.
                     */
                    adapter = ContactAdapter(Klaxon().parseArray(it) ?: emptyList())
                    addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                }
            }
        }
        /**
         * Exception handling with user notification via Snackbar [https://material.io/components/snackbars].
         */
        catch (e: FileNotFoundException) {
            Log.e("APP", "Failed to open dataset file", e)
            Snackbar.make(
                binding.recyclerviewContactList,
                "Failed to access contacts data",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        } catch (e: KlaxonException) {
            Log.e("APP", "Failed to parse dataset file", e)
            Snackbar.make(
                binding.recyclerviewContactList,
                "Failed to read contacts data",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }
}