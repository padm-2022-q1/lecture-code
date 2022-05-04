package br.edu.ufabc.todostorage.model

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A repository that persists the data in a file.
 */
class RepositoryFile(application: Application, autoGenerateIds: Boolean = true) : Repository {
    private val delegate = RepositoryInMemory(autoGenerateIds = autoGenerateIds)
    private val cacheFile = File(application.applicationContext.cacheDir, "tasks.json")
    private val hasLoaded = AtomicBoolean(false)
    private val mutex = Mutex()

    private suspend fun load() = withContext(Dispatchers.IO) {
        runCatching {
            cacheFile.createNewFile()
            cacheFile.inputStream().use { inputStream ->
                inputStream.bufferedReader().readText().takeIf { it.isNotEmpty() }?.let { text ->
                    Gson().fromJson<Tasks>(text, object : TypeToken<Tasks>() {}.type).let { tasks ->
                        tasks.forEach {
                            delegate.add(it)
                        }
                    }
                }
            }
        }.onFailure { e ->
            throw Exception("Failed to load data from file", e)
        }
    }

    private suspend fun save() = withContext(Dispatchers.IO) {
        mutex.withLock {
            runCatching {
                cacheFile.outputStream().use { outputStream ->
                    outputStream.bufferedWriter().let {
                        it.write(Gson().toJson(delegate.getAll()))
                        it.flush()
                    }
                }
            }.onFailure { e ->
                throw Exception("Failed to save data to file", e)
            }
        }
    }

    private suspend fun getDelegate(): RepositoryInMemory = mutex.withLock {
        if (!hasLoaded.get()) {
            load()
            hasLoaded.set(true)
        }
        return delegate
    }

    override suspend fun getAll(): Tasks = getDelegate().getAll()

    override suspend fun getPending(): Tasks = getDelegate().getPending()

    override suspend fun getById(id: Long): Task = getDelegate().getById(id)

    override suspend fun getOverdue(): Tasks = getDelegate().getOverdue()

    override suspend fun getCompleted(): Tasks = getDelegate().getCompleted()

    override suspend fun getTags(): List<String> = getDelegate().getTags()

    override suspend fun getByTag(tag: String): Tasks = getDelegate().getByTag(tag)

    override suspend fun update(task: Task) {
        getDelegate().update(task)
        save()
    }

    override suspend fun add(task: Task): Long =
        getDelegate().add(task).also {
            save()
        }

    override suspend fun removeById(id: Long) {
        getDelegate().removeById(id)
        save()
    }

    override suspend fun removeAll() {
        getDelegate().removeAll()
        save()
    }

    override suspend fun refresh() {
        getDelegate().refresh()
    }
}
