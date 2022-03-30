package br.edu.ufabc.listacontatosmvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.listacontatosmvvm.databinding.FragmentContactItemBinding


class ContactItemFragment : Fragment() {
    private lateinit var binding: FragmentContactItemBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            viewModel.clickedItemId.value?.let { contactId ->
                viewModel.getById(contactId)?.let { contact ->
                    binding.contactListItemFullname.text = contact.name
                    binding.contactListItemPhoneValue.text = contact.phone
                    binding.contactListItemEmailValue.text = contact.email
                    binding.contactListItemAddressValue.text = contact.address
                } ?: throw Exception("Failed to find contact with id $contactId")
            } ?: throw Exception("Could not obtain contact id")
        } catch (e: Exception) {
            Log.e("VIEW", "Failed to create item detail view", e)
            binding.root.visibility = View.INVISIBLE
        }

    }
}