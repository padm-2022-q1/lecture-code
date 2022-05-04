package br.edu.ufabc.todostorage.model.room

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.withTransaction
import br.edu.ufabc.todostorage.model.Repository
import br.edu.ufabc.todostorage.model.Task
import br.edu.ufabc.todostorage.model.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * A repository that stores data in a Room database.
 */
class RepositoryRoom(application: Application) : Repository {
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(application, AppDatabase::class.java, "tasks").build()
    }

    override suspend fun getAll(): Tasks = withContext(Dispatchers.IO) {
        db.taskTagDao().getAll().map { it.toTask() }
    }

    override suspend fun getPending(): Tasks = withContext(Dispatchers.IO) {
        db.taskTagDao().getPending().map { it.toTask() }
    }

    override suspend fun getById(id: Long): Task = withContext(Dispatchers.IO) {
        db.taskTagDao().getById(id).toTask()
    }

    override suspend fun getOverdue(): Tasks = withContext(Dispatchers.IO) {
        fun simpleNow(): Long =
            SimpleDateFormat("dd/MM/yyyy", Locale.US).let { formatter ->
                formatter.parse(formatter.format(Date()))?.time ?: 0
            }

        db.taskTagDao().getOverdue(simpleNow()).map { it.toTask() }
    }

    override suspend fun getCompleted(): Tasks = withContext(Dispatchers.IO) {
        db.taskTagDao().getCompleted().map { it.toTask() }
    }

    override suspend fun getTags(): List<String> = withContext(Dispatchers.IO) {
        db.tagDao().list().map { it.name }
    }

    override suspend fun getByTag(tag: String): Tasks = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.taskTagDao().getByTag(tag).tasks.map { taskRoom ->
                db.taskTagDao().getById(taskRoom.id)
            }.map { it.toTask() }
        }
    }

    override suspend fun update(task: Task) {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                removeById(task.id)
                add(task)
            }
        }
    }

    override suspend fun add(task: Task): Long = withContext(Dispatchers.IO) {
        db.withTransaction {
            val taskId = db.taskDao().insert(TaskRoom.fromTask(task))

            task.tags?.forEach { name ->
                val tagId = try {
                    db.tagDao().getByName(name).id
                } catch (_: Exception) {
                    db.tagDao().insert(Tag(id = 0, name = name))
                }
                TaskTag(
                    taskId = taskId,
                    tagId = tagId
                ).let {
                    db.taskTagDao().insert(it)
                }
            }

            taskId
        }
    }

    override suspend fun removeById(id: Long) = withContext(Dispatchers.IO) {
        db.withTransaction {
            val tags = db.taskTagDao().getById(id).tags

            db.taskDao().deleteById(id)
            tags.forEach { tag ->
                db.taskTagDao().getByTag(tag.name).tasks.takeIf { it.isEmpty() }?.let {
                    db.tagDao().deleteById(tag.id)
                }
            }
        }
    }

    override suspend fun removeAll() = withContext(Dispatchers.IO) {
        db.clearAllTables()
    }

    override suspend fun refresh() {
        Log.i("NOT IMPLEMENTED", "NOT IMPLEMENTED")
    }
}
