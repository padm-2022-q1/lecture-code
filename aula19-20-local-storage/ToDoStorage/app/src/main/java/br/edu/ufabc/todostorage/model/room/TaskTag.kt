package br.edu.ufabc.todostorage.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Entity representing the join table between tag and task.
 * @property taskId the task id
 * @property tagId the tag id
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TaskRoom::class,
            childColumns = ["taskId"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            childColumns = ["tagId"],
            parentColumns = ["id"]
        )
    ],
    primaryKeys = ["taskId", "tagId"]
)
data class TaskTag(
    val taskId: Long,
    @ColumnInfo(index = true)
    val tagId: Long
)
