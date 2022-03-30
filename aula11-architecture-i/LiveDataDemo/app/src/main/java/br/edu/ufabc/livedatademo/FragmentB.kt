package br.edu.ufabc.livedatademo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import br.edu.ufabc.livedatademo.databinding.FragmentBBinding


/**
 * A simple [Fragment] subclass.
 */
class FragmentB : Fragment() {
    private lateinit var binding: FragmentBBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val observer = Observer<Int> {
            binding.fragmentBMessage.text = getString(R.string.fragment_b_message, it)
        }
        binding.switchB.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.counter.observe(viewLifecycleOwner, observer)
            } else {
                viewModel.counter.removeObserver(observer)
            }
        }
    }

}
