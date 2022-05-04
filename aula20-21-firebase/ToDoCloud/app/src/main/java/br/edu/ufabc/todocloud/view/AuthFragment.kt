package br.edu.ufabc.todocloud.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.todocloud.databinding.FragmentAuthBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                launchTaskList()
            } else {
                Snackbar.make(binding.root, "Failed to sign-in. Please try again.",
                    Snackbar.LENGTH_LONG).show()
            }
        }
        authenticate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignIn.setOnClickListener {
            launchAuthUi()
        }
    }

    private fun authenticate() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            launchTaskList()
        }
    }

    private fun launchTaskList() = AuthFragmentDirections.showList().let {
        findNavController().navigate(it)
    }

    private fun launchAuthUi() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        launcher.launch(intent)
    }
}