package br.edu.ufabc.livedatademo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.edu.ufabc.livedatademo.databinding.FragmentCBinding


/**
 * A simple [Fragment] subclass.
 */
class FragmentC : Fragment() {
    private lateinit var binding: FragmentCBinding
    private val viewModel : MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendMessageButton.setOnClickListener {
            viewModel.counter.value?.let {
                viewModel.counter.value = it + 1
            }
        }
    }
}
