package br.edu.ufabc.listacontatosclient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.listacontatosclient.databinding.FragmentContactItemBinding
import com.google.android.material.snackbar.Snackbar


class ContactItemFragment : Fragment() {
    private lateinit var binding: FragmentContactItemBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: ContactItemFragmentArgs by navArgs()

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
            args.contactId.takeIf { it >= 0 }?.also { contactId ->
                viewModel.isLoading.value = true
                binding.root.visibility = View.INVISIBLE
                viewModel.getById(contactId).observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        is MainViewModel.Status.Success -> {
                            val contact = result.result

                            binding.contactListItemFullname.text = contact?.name
                            binding.contactListItemPhoneValue.text = contact?.phone
                            binding.contactListItemEmailValue.text = contact?.email
                            binding.contactListItemAddressValue.text = contact?.address

                            binding.root.visibility = View.VISIBLE
                        }
                        is MainViewModel.Status.Error -> {
                            Log.e("VIEW", "Failed to create item detail view", result.status.e)
                            Snackbar.make(view.rootView, "Failed to fetch item",
                                Snackbar.LENGTH_LONG).show()
                        }
                    }
                    viewModel.isLoading.value = false
                }
            } ?: throw Exception("Could not obtain a valid contact id")
        } catch (e: Exception) {
            Log.e("VIEW", "Failed to create item detail view", e)
            Snackbar.make(view.rootView, "No valid id was provided",
                Snackbar.LENGTH_LONG).show()
            binding.root.visibility = View.INVISIBLE
        }

    }
}