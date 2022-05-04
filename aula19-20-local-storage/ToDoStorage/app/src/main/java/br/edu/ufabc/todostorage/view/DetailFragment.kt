package br.edu.ufabc.todostorage.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.edu.ufabc.todostorage.R
import br.edu.ufabc.todostorage.databinding.FragmentDetailBinding
import br.edu.ufabc.todostorage.model.Task
import br.edu.ufabc.todostorage.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment to show the details of a task.
 */
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private val taskCache = MutableLiveData<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun fillTable() = taskCache.observe(viewLifecycleOwner) { task ->
        binding.title.text = task.title
        task.formattedDeadline().let {
            binding.deadline.text = it
            binding.deadlineRow.visibility = View.VISIBLE
        }
        task.tags?.sorted()?.apply {
            binding.tagsRow.visibility = View.VISIBLE
            forEach { tag ->
                LayoutInflater.from(binding.tagsContainer.context)
                    .inflate(R.layout.chip, binding.tagsContainer, false).let { view ->
                        (view as Chip).apply {
                            text = tag
                            setOnClickListener {
                                DetailFragmentDirections.onTagClick(
                                    FilterCriteria.TAG,
                                    "Tag: $tag",
                                    tag
                                ).let {
                                    findNavController().navigate(it)
                                }
                            }
                            binding.tagsContainer.addView(this)
                        }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.root.visibility = View.INVISIBLE
        viewModel.getById(args.taskId).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> try {
                    val task = (status.result as MainViewModel.Result.SingleTask).value

                    this.taskCache.value = task
                    fillTable()
                    binding.root.visibility = View.VISIBLE
                    binding.progressHorizontal.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    Log.e("FRAGMENT", "Failed to render item", e)
                    notifyError("Failed to show item")
                }
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e(
                        "VIEW",
                        "Failed to obtain and render item with id ${args.taskId}",
                        status.e
                    )
                    notifyError("Failed to fetch and show task")
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setComplete(state: Boolean) = taskCache.observe(viewLifecycleOwner) { task ->
        viewModel.update(
            Task(
                id = task.id,
                title = task.title,
                deadline = task.deadline,
                tags = task.tags,
                completed = state
            )
        ).observe(viewLifecycleOwner) { result ->
            when (result) {
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Success -> {
                    DetailFragmentDirections.onCompleteSuccess(task.id).let {
                        findNavController().navigate(it)
                    }
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("VIEW", "Failed to change completion status", result.e)
                    Snackbar.make(
                        binding.root,
                        "Failed to change completion status",
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun remove() = taskCache.observe(viewLifecycleOwner) { task ->
        viewModel.remove(task.id).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Success -> {
                    DetailFragmentDirections.onRemoveSuccess().let {
                        findNavController().navigate(it)
                    }
                }
                is MainViewModel.Status.Loading -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                }
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to remove item", status.e)
                    notifyError("Failed to remove item")
                }
            }
        }
    }

    private fun notifyError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun makeConfirmationDialog() = MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.title_delete_contact_confirmation))
        .setMessage(getString(R.string.message_delete_title_confirmation))
        .setNegativeButton(getString(R.string.cancel_delete_contact_label)) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(getString(R.string.confirm_delete_contact_label)) { dialog, _ ->
            remove()
            dialog.dismiss()
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        taskCache.observe(viewLifecycleOwner) { task ->
            if (task.completed) {
                menu.findItem(R.id.action_undo).isVisible = true
            } else {
                menu.findItem(R.id.action_complete).isVisible = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_complete -> setComplete(true)
            R.id.action_undo -> setComplete(false)
            R.id.action_remove -> makeConfirmationDialog().show()
            R.id.action_edit -> DetailFragmentDirections.editItem(args.taskId).let {
                findNavController().navigate(it)
            }
        }
        return true
    }
}
