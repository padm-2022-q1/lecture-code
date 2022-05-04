package br.edu.ufabc.todostorage.model.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.edu.ufabc.todostorage.model.Task

/**
 * Relationship between a task and its tags.
 * @property task the task
 * @property tags the tags
 */
data class TaskWithTags(
    @Embedded
    val task: TaskRoom,

    @Relation(
        associateBy = Junction(
            TaskTag::class,
            parentColumn = "taskId",
            entityColumn = "tagId"
        ),
        parentColumn = "id",
        entityColumn = "id",
        entity = Tag::class
    )
    val tags: List<Tag>
) {
    /**
     * Converts the room model Task to the domain model Task.
     * @return the domain model task
     */
    fun toTask() = Task(
        id = task.id,
        title = task.title,
        deadline = task.deadline,
        completed = task.completed,
        tags = tags.map { it.name }
    )
}
