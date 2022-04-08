package br.edu.ufabc.todo.model

import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Repository {
    private lateinit var tasks: List<Task>

    private data class TaskJson(
        var id: Long,
        var title: String,
        var deadline: String,
        var completed: String,
        var tags: String
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
        fun toTask() = Task(
            id,
            title,
            formatter.parse(deadline) ?: Date(),
            completed == "Y",
            tags.split(",").map { it.trim() }
        )
    }

    fun loadData(inputStream: InputStream) {
        tasks = Klaxon().parseArray<TaskJson>(inputStream)?.map { it.toTask()} ?: emptyList()
    }

    private fun validTasks() = if (this::tasks.isInitialized) tasks else
        throw Exception("Repository has not loaded data yet")

    val getAll = ::getPending

    fun getById(id: Long): Task? = validTasks().find {
        it.id == id
    }

    fun getOverdue(): List<Task> = getPending().filter { it.deadline.before(Date()) && !it.completed }

    fun getPending(): List<Task> = validTasks().filter { !it.completed }

    fun getTags(): List<String> = mutableSetOf<String>().also {
            getPending().forEach { task ->
                it.addAll(task.tags)
            }
        }.toList()

    fun getByTag(tag: String): List<Task> = mutableListOf<Task>().also {
        getPending().forEach { task ->
            if (task.tags.contains(tag))
                it.add(task)
        }
    }

    fun getCompleted(): List<Task> = validTasks().filter { it.completed }
}