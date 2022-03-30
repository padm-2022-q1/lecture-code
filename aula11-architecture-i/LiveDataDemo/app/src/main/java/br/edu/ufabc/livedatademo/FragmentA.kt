package br.edu.ufabc.livedatademo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import br.edu.ufabc.livedatademo.databinding.FragmentABinding


/**
 * A simple [Fragment] subclass.
 */
class FragmentA : Fragment() {
    private lateinit var binding: FragmentABinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val observer = Observer<Int> {
            Log.d("APP", it.toString())
            binding.fragmentAMessage.text = getString(R.string.fragment_a_message, it)
        }
        binding.switchA.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.counter.observe(viewLifecycleOwner, observer)
            } else {
                viewModel.counter.removeObserver(observer)
            }
        }
    }
}
