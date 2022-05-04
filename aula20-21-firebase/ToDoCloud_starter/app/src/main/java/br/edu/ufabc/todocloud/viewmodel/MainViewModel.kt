package br.edu.ufabc.todocloud.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.todocloud.model.RepositoryFactory
import br.edu.ufabc.todocloud.model.Task
import br.edu.ufabc.todocloud.model.Tasks

/**
 * The shared view model.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryFactory(application).create()

    private val _shouldRefresh = MutableLiveData(true)

    /**
     * Live data to be observed by views interested on being refreshed.
     */
    val shouldRefresh: LiveData<Boolean> = _shouldRefresh

    /**
     * Status hierarchy.
     */
    sealed class Status {
        /**
         * The error status.
         * @property e the exception
         */
        class Failure(val e: Exception) : Status()

        /**
         * The success status.
         * @property result the result
         */
        class Success(val result: Result) : Status()

        /**
         * The loading status.
         */
        object Loading : Status()
    }

    /**
     * Refresh status hierarchy.
     */
    sealed class RefreshStatus {
        /**
         * The loading status.
         */
        object Loading : RefreshStatus()

        /**
         * The done status.
         */
        object Done : RefreshStatus()

        /**
         * The failure status.
         * @property e the exception
         */
        class Failure(val e: Exception) : RefreshStatus()
    }

    /**
     * The result hierarchy.
     */
    sealed class Result {
        /**
         * Result type that holds a list of tasks.
         * @property value the list of tasks
         */
        data class TaskList(
            val value: Tasks
        ) : Result()

        /**
         * Result type that holds a single task.
         * @property value the task
         */
        data class SingleTask(
            val value: Task
        ) : Result()

        /**
         * Result type that holds a list of tags.
         * @property value the list of tags
         */
        data class TagList(
            val value: List<String>
        ) : Result()

        /**
         * Result type that holds an id.
         * @property value the id.
         */
        data class Id(
            val value: Long
        ) : Result()

        /**
         * A Result without value.
         */
        object EmptyResult : Result()
    }

    /**
     * Command the view state to refresh.
     */
    fun refresh() = liveData {
        try {
            emit(RefreshStatus.Loading)
            repository.refresh()
            _shouldRefresh.postValue(true)
            emit(RefreshStatus.Done)
        } catch (e: Exception) {
            emit(RefreshStatus.Failure(Exception("Failed to refresh data", e)))
        }
    }

    /**
     * Get pending tasks (async).
     */
    fun getPending() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getPending())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    /**
     * Get overdue tasks (async).
     */
    fun getOverdue() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getOverdue())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch overdue items from repository", e)))
        }
    }

    /**
     * Get completed tasks (async).
     */
    fun getCompleted() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getCompleted())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch completed items from repository", e)))
        }
    }

    /**
     * Get a list of tasks that contain a specific tag (async).
     */
    fun getByTag(tag: String) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getByTag(tag))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch items with the given tag", e)))
        }
    }

    /**
     * Get a list of all tags.
     */
    fun getTags() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TagList(repository.getTags())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch tags", e)))
        }
    }

    /**
     * Get a specific task given its id.
     */
    fun getById(id: Long) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.SingleTask(repository.getById(id))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to get element by id", e)))
        }
    }

    /**
     * Update a task.
     */
    fun update(task: Task) = liveData {
        try {
            emit(Status.Loading)
            repository.update(task)
            emit(Status.Success(Result.EmptyResult))
            _shouldRefresh.postValue(true)
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to update element", e)))
        }
    }

    /**
     * Add a new task.
     * @param task the task
     * @return the async status
     */
    fun add(task: Task) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.add(task))))
            _shouldRefresh.postValue(true)
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to add element", e)))
        }
    }

    /**
     * Remove a task given its id.
     * @param id the id
     * @return the async status
     */
    fun remove(id: Long) = liveData {
        try {
            emit(Status.Loading)
            repository.removeById(id)
            emit(Status.Success(Result.EmptyResult))
            _shouldRefresh.postValue(true)
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to remove element", e)))
        }
    }
}
