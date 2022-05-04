package br.edu.ufabc.todostorage.model.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Represents the relationship between a tag and its tasks.
 * @property tag the tag
 * @property tasks the tasks
 */
data class TagWithTasks(
    @Embedded
    val tag: Tag,

    @Relation(
        associateBy = Junction(
            TaskTag::class,
            parentColumn = "tagId",
            entityColumn = "taskId"
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = TaskRoom::class
    )
    val tasks: List<TaskRoom>
)
