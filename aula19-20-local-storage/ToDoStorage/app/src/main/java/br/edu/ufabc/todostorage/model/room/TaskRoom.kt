package br.edu.ufabc.todostorage.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ufabc.todostorage.model.Task
import java.util.*

/**
 * The task entity.
 * @property id the id
 * @property title the title
 * @property deadline the deadline
 * @property completed the completion status
 */
@Entity
data class TaskRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val deadline: Date?,
    val completed: Boolean
) {
    companion object {
        /**
         * Converts a domain model Task to Room model task.
         * @param task the domain model task
         * @return a Room model task
         */
        fun fromTask(task: Task) = TaskRoom(
            id = task.id,
            title = task.title,
            deadline = task.deadline,
            completed = task.completed
        )
    }
}
