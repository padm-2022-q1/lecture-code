package br.edu.ufabc.todostorage.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import br.edu.ufabc.todostorage.R
import br.edu.ufabc.todostorage.databinding.FragmentAddEditBinding
import br.edu.ufabc.todostorage.model.Task
import br.edu.ufabc.todostorage.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Task inserting screen.
 */
class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.deadline.text = Task.formatDate(Date())

        binding.deadline.setOnClickListener {
            DatePickerFragment(binding.deadline).show(
                requireActivity().supportFragmentManager,
                null
            )
        }
    }

    private fun validate(): Boolean =
        binding.edittextTitle.let { field ->
            field.text.toString().trim().isNotEmpty().also {
                field.error = if (!it) "Required field" else null
            }
        }

    private fun parseForm() = Task(
        id = 0,
        title = binding.edittextTitle.text.toString(),
        deadline = Task.parseDate(binding.deadline.text.toString()),
        tags = binding.edittextTags.text?.split(",")?.map { it.trim() }
            ?.filter { it.isNotEmpty() },
        completed = false
    )

    private fun save() {
        parseForm().let { task ->
            viewModel.add(task).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Success -> {
                        val insertedId = (status.result as MainViewModel.Result.Id).value
                        AddFragmentDirections.onAddSuccess(insertedId).let {
                            findNavController().navigate(it, navOptions {
                                popUpTo(R.id.destination_list)
                            })
                        }
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                    is MainViewModel.Status.Loading -> {
                        binding.progressHorizontal.visibility = View.VISIBLE
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("FRAGMENT", "Failed to add item", status.e)
                        Snackbar.make(binding.root, "Failed to add item", Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                }
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                if (validate()) save()
            }
        }
        return true
    }
}
