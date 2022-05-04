package br.edu.ufabc.todostorage.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import br.edu.ufabc.todostorage.R
import br.edu.ufabc.todostorage.databinding.FragmentAddEditBinding
import br.edu.ufabc.todostorage.model.Task
import br.edu.ufabc.todostorage.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Task editing screen.
 */
class EditFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBinding
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private val taskCache = MutableLiveData<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()

        binding.deadline.setOnClickListener {
            DatePickerFragment(binding.deadline).show(
                requireActivity().supportFragmentManager,
                null
            )
        }

        fetchTask()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun fillForm() = taskCache.observe(viewLifecycleOwner) { task ->
        binding.edittextTitle.setText(task.title)
        binding.deadline.let {
            binding.deadlineRow.visibility = View.VISIBLE
            it.text = task.formattedDeadline()
        }
        task.tags?.apply {
            binding.tagsRow.visibility = View.VISIBLE
            binding.edittextTags.setText(task.tags.joinToString(", "))
        }
    }

    private fun fetchTask() = viewModel.getById(args.taskId).observe(viewLifecycleOwner) { status ->
        when (status) {
            is MainViewModel.Status.Success -> {
                taskCache.value = (status.result as MainViewModel.Result.SingleTask).value
                fillForm()
            }
            is MainViewModel.Status.Loading -> binding.progressHorizontal.visibility = View.VISIBLE
            is MainViewModel.Status.Failure -> {
                Log.e("FRAGMENT", "Failed to fetch item with id ${args.taskId} to update", status.e)
                notifyError("Failed to show item")
            }
        }
        binding.progressHorizontal.visibility = View.INVISIBLE
    }

    private fun validateForm(): Boolean {
        binding.edittextTitle.apply {
            if (text?.trim()?.isEmpty() == true) {
                error = "Required field"
                return false
            }
            return true
        }
    }

    private fun save() = taskCache.observe(viewLifecycleOwner) { task ->
        Task(
            id = task.id,
            title = binding.edittextTitle.text?.trim()?.toString() ?: "",
            deadline = Task.parseDate(binding.deadline.text.toString()),
            tags = binding.edittextTags.text?.split(",")?.map { it.trim() },
            completed = task.completed
        ).let { updatedTask ->
            viewModel.update(updatedTask).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Success -> {
                        EditFragmentDirections.onEditSuccess(updatedTask.id).let {
                            findNavController().navigate(it, navOptions {
                                popUpTo(R.id.destination_list)
                            })
                        }
                    }
                    is MainViewModel.Status.Loading ->
                        binding.progressHorizontal.visibility = View.VISIBLE
                    is MainViewModel.Status.Failure -> {
                        Log.e("FRAGMENT", "Failed to update item", status.e)
                        notifyError("Failed to update item")
                    }
                }
                binding.progressHorizontal.visibility = View.INVISIBLE
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> if (validateForm()) save()
        }
        return true
    }
}
