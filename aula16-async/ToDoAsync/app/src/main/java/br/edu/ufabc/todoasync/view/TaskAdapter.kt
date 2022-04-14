package br.edu.ufabc.todoasync.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.todoasync.R
import br.edu.ufabc.todoasync.databinding.ListItemBinding
import br.edu.ufabc.todoasync.model.Task
import java.util.*

class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val title = itemBinding.textviewTitle
        val tagsContainer = itemBinding.containerTags
        val deadline = itemBinding.textviewDeadline
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.title.text = task.title
        holder.deadline.text = task.formattedDeadline()
        if (task.completed)
            holder.title.setTextColor(ContextCompat.getColor(holder.title.context, R.color.gray))
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
                    task.deadline.before(Date()) -> R.color.red
                    else -> R.color.black
                }
            )
        )
        task.tags.sorted().forEach { tag ->
            LayoutInflater.from(holder.tagsContainer.context)
                .inflate(R.layout.tag_item, holder.tagsContainer, false)?.let {
                    (it as TextView).text = tag

                    holder.tagsContainer.addView(it)
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