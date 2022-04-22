package br.edu.ufabc.flickrgallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.flickrgallery.databinding.FragmentPhotoBinding
import com.google.android.material.snackbar.Snackbar

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: PhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.downloadPhoto(args.photoUrl).observe(viewLifecycleOwner) { result ->
            when (result.status) {
                is MainViewModel.Status.Success -> {
                    binding.photoFull.setImageBitmap(result.result)
                }
                is MainViewModel.Status.Error -> {
                    Log.e("FRAGMENT", "Could not render bitmap", result.status.e)
                    Snackbar.make(binding.root, "Could not show image",
                        Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

}