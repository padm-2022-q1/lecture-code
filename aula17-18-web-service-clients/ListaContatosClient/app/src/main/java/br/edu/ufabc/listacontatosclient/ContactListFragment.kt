package br.edu.ufabc.listacontatosclient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.listacontatosclient.databinding.ContactListItemBinding
import br.edu.ufabc.listacontatosclient.databinding.FragmentContactListBinding
import br.edu.ufabc.listacontatosclient.model.Contact


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
                    ContactListFragmentDirections
                        .showContactDetails(getItemId(bindingAdapterPosition)).let {
                            findNavController().navigate(it)
                        }
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

    private fun refresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.allContacts().observe(viewLifecycleOwner) { result ->
            when (result.status) {
                is MainViewModel.Status.Success -> {
                    binding.recyclerviewContactList.apply {
                        adapter = ContactAdapter(result.result ?: emptyList())
                        addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
                    }
                }
                is MainViewModel.Status.Error -> {}
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onStart() {
        super.onStart()
        refresh()
        binding.swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
        binding.fabAddContact.setOnClickListener {
            ContactListFragmentDirections.addContact().let {
                findNavController().navigate(it)
            }
        }
    }
}