package br.edu.ufabc.todostorage.model

import android.util.Log
import java.util.*

/**
 * A repository that maintains the data on primary memory.
 */
class RepositoryInMemory(private val autoGenerateIds: Boolean = true) : Repository {
    private val tasks = mutableListOf<Task>()

    override suspend fun getAll(): Tasks = tasks

    override suspend fun getPending(): Tasks = tasks.filter { !it.completed }

    override suspend fun getById(id: Long): Task = tasks.find { it.id == id }
        ?: throw Exception("Failed to find task with id $id")

    override suspend fun getOverdue(): Tasks = tasks.filter {
        !it.completed && it.deadline?.before(Task.simplifyDate(Date())) ?: false
    }

    override suspend fun getCompleted(): Tasks = tasks.filter { it.completed }

    override suspend fun getTags(): List<String> = mutableSetOf<String>().also { set ->
        tasks.forEach { task -> set.addAll(task.tags ?: emptyList()) }
    }.toList()

    override suspend fun getByTag(tag: String): Tasks = tasks.filter { it.tags?.contains(tag) ?: false }

    override suspend fun update(task: Task) {
        tasks.find { it.id == task.id }?.let { oldTask ->
            tasks.remove(oldTask)
            tasks.add(task)
        } ?: throw Exception("Failed to edit item with non-existing id $task.id")
    }

    override suspend fun add(task: Task): Long = Task(
        id = if (autoGenerateIds) nextId() else task.id,
        title = task.title,
        deadline = task.deadline,
        tags = task.tags,
        completed = task.completed
    ).let { newTask ->
        tasks.add(newTask)
        newTask.id
    }

    override suspend fun removeById(id: Long) {
        tasks.find { it.id == id }?.let {
            tasks.remove(it)
        } ?: throw Exception("Failed to remove item with non-existing id $id")
    }

    override suspend fun removeAll() {
        tasks.clear()
    }

    override suspend fun refresh() {
        Log.i("NOT IMPLEMENTED", "NOT IMPLEMENTED")
    }

    private fun nextId() = tasks.maxByOrNull { it.id }?.let { it.id + 1 } ?: 1
}
