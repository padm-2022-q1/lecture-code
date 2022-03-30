package br.edu.ufabc.listacontatosresponsiva

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.ufabc.listacontatosresponsiva.databinding.FragmentContactItemBinding


class ContactItemFragment : Fragment() {
    private lateinit var binding: FragmentContactItemBinding

    companion object {
        const val contactPosition = "contactPosition"
    }

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
            arguments?.let { bundle ->
                val contactPosition = bundle.getInt(ContactItemFragment.contactPosition)

                activity?.let { activity ->
                    val contact = (activity.application as App).repository.getAll()[contactPosition]

                    binding.contactListItemFullname.text = contact.name
                    binding.contactListItemPhoneValue.text = contact.phone
                    binding.contactListItemEmailValue.text = contact.email
                    binding.contactListItemAddressValue.text = contact.address
                } ?: throw Exception("Failed to access host activity")
            } ?: throw Exception("Failed to access fragment arguments")
        } catch (e: Exception) {
            Log.e("VIEW", "Failed to create item detail view", e)
            binding.root.visibility = View.INVISIBLE
        }

    }
}