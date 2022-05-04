package br.edu.ufabc.todostorage.model

import android.app.Application

/**
 * A repository that maintains a local cache.
 */
class RepositoryCached(application: Application) : Repository {
    private val repositoryRemote = RepositoryFactory(application).create(RepositoryFactory.Type.WebService)
    private val repositoryLocal = RepositoryFactory(application).create(RepositoryFactory.Type.Room)

    override suspend fun getAll(): Tasks = repositoryLocal.getAll()

    override suspend fun getPending(): Tasks = repositoryLocal.getPending()

    override suspend fun getById(id: Long): Task = repositoryLocal.getById(id)

    override suspend fun getOverdue(): Tasks = repositoryLocal.getOverdue()

    override suspend fun getCompleted(): Tasks = repositoryLocal.getCompleted()

    override suspend fun getTags(): List<String> = repositoryLocal.getTags()

    override suspend fun getByTag(tag: String): Tasks = repositoryLocal.getByTag(tag)

    override suspend fun update(task: Task) = try {
        repositoryRemote.update(task)
        repositoryLocal.update(task)
    } catch (e: Exception) {
        throw Exception("Failed to update task", e)
    }

    override suspend fun add(task: Task): Long = try {
        repositoryRemote.add(task).let { insertedId ->
            Task(
                id = insertedId,
                title = task.title,
                deadline = task.deadline,
                completed = task.completed,
                tags = task.tags
            ).let {
                repositoryLocal.add(it)
            }
        }
    } catch (e: Exception) {
        throw Exception("Failed to add task. Device offline?", e)
    }

    override suspend fun removeById(id: Long) = try {
        repositoryRemote.removeById(id)
        repositoryLocal.removeById(id)
    } catch (e: Exception) {
        throw Exception("Failed to delete task. Device offline?", e)
    }

    override suspend fun removeAll() {
        repositoryRemote.removeAll()
        repositoryLocal.removeAll()
    }

    override suspend fun refresh() {
        val tasks = repositoryRemote.getAll()

        repositoryLocal.removeAll()
        tasks.forEach { task -> repositoryLocal.add(task) }
    }
}
