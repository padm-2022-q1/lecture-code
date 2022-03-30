package br.edu.ufabc.listacontatosmvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.listacontatosmvvm.databinding.ContactListItemBinding
import br.edu.ufabc.listacontatosmvvm.databinding.FragmentContactListBinding
import br.edu.ufabc.listacontatosmvvm.model.Contact


class ContactListFragment : Fragment() {
    private lateinit var  binding: FragmentContactListBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * Adapter.
     */
    private inner class ContactAdapter(val contacts: List<Contact>) :
        RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

        /**
         * View Holder.
         */
        private inner class ContactHolder(itemBinding: ContactListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val name = itemBinding.contactListItemFullname

            init {
                itemBinding.root.setOnClickListener {
                    viewModel.clickedItemId.value = getItemId(bindingAdapterPosition)
                }
            }
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
        }

        /**
         * The total quantity of items in the list.
         */
        override fun getItemCount(): Int = contacts.size

        override fun getItemId(position: Int): Long = contacts[position].id

        /**
         * Called when a view holder is recycled.
         */
        override fun onViewRecycled(holder: ContactHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.bindingAdapterPosition}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            binding.recyclerviewContactList.apply {
                adapter = ContactAdapter(viewModel.allContacts())
                addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            }
        }

    }
}