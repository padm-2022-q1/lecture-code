package br.edu.ufabc.todostorage.view

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.todostorage.R
import br.edu.ufabc.todostorage.databinding.FragmentListBinding
import br.edu.ufabc.todostorage.databinding.ListItemBinding

import br.edu.ufabc.todostorage.model.Task
import br.edu.ufabc.todostorage.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Fragment to show lists of tasks.
 */
class ListFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding
    private val args: ListFragmentArgs by navArgs()

    /**
     * Adapter for task lists.
     */
    inner class TaskAdapter(private val tasks: List<Task>) :
        RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        /**
         * ViewHolder for a task adapter.
         */
        inner class TaskViewHolder(itemBinding: ListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            /**
             * The title field.
             */
            val title = itemBinding.textviewTitle

            /**
             * The layout that store tags.
             */
            val tagsContainer = itemBinding.containerTags

            /**
             * The deadline field.
             */
            val deadline = itemBinding.textviewDeadline

            init {
                itemBinding.root.setOnClickListener {
                    ListFragmentDirections.showDetail(getItemId(bindingAdapterPosition)).let {
                        findNavController().navigate(it)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                ListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = tasks[position]

            holder.title.text = task.title
            holder.deadline.text = if (task.deadline != null) task.formattedDeadline() else ""
            if (task.completed)
                holder.title.setTextColor(
                    ContextCompat.getColor(
                        holder.title.context,
                        R.color.gray
                    )
                )
            holder.title.paintFlags =
                if (task.completed)
                    holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.deadline.setTextColor(
                ContextCompat.getColor(
                    holder.deadline.context,
                    when {
                        task.completed -> R.color.gray
                        task.deadline?.before(Task.simplifyDate(Date())) ?: false -> R.color.red
                        else -> R.color.black
                    }
                )
            )
            task.tags?.sorted()?.forEach { tag ->
                LayoutInflater.from(holder.tagsContainer.context)
                    .inflate(R.layout.chip, holder.tagsContainer, false).let { view ->
                        (view as Chip).apply {
                            text = tag
                            setOnClickListener {
                                ListFragmentDirections.onTagClick(
                                    FilterCriteria.TAG,
                                    "Tag: $tag",
                                    tag
                                )
                                    .let {
                                        findNavController().navigate(it)
                                    }
                            }
                            holder.tagsContainer.addView(this)
                        }
                    }
            }
        }

        override fun onViewRecycled(holder: TaskViewHolder) {
            super.onViewRecycled(holder)
            holder.tagsContainer.removeAllViews()
        }

        override fun getItemCount(): Int = tasks.size

        override fun getItemId(position: Int): Long = tasks[position].id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.shouldRefresh.observe(viewLifecycleOwner) {
            if (it) refresh()
        }
        binding.fabAddTask.setOnClickListener {
            ListFragmentDirections.newTask().let {
                findNavController().navigate(it)
            }
        }
    }

    private fun refresh() {
        binding.recyclerViewList.apply {
            when (args.filterCriteria) {
                FilterCriteria.ALL -> viewModel.getPending()
                FilterCriteria.OVERDUE -> viewModel.getOverdue()
                FilterCriteria.COMPLETED -> viewModel.getCompleted()
                FilterCriteria.TAG -> viewModel.getByTag(args.tag)
            }.observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> {
                        binding.progressHorizontal.visibility = View.VISIBLE
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("VIEW", "Failed to fetch items", status.e)
                        Snackbar.make(binding.root, "Failed to list items", Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                    is MainViewModel.Status.Success -> {
                        val tasks = (status.result as MainViewModel.Result.TaskList).value

                        swapAdapter(TaskAdapter(tasks.sortedBy { it.deadline }), false)
                        addItemDecoration(
                            DividerItemDecoration(
                                this.context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> viewModel.refresh().observe(this) { refreshStatus ->
                when (refreshStatus) {
                    is MainViewModel.RefreshStatus.Loading -> binding.progressHorizontal.visibility =
                        View.VISIBLE
                    is MainViewModel.RefreshStatus.Done -> binding.progressHorizontal.visibility =
                        View.INVISIBLE
                    is MainViewModel.RefreshStatus.Failure -> {
                        Log.e("VIEW", "Failed to refresh list", refreshStatus.e)
                        Snackbar.make(binding.root, "Failed to refresh data", Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                }
            }
        }
        return true
    }
}
