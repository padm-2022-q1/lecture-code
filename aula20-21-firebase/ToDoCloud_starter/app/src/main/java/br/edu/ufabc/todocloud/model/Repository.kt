package br.edu.ufabc.todocloud.model

/**
 * General API for repositories.
 */
interface Repository {

    /**
     * Fetch a list of all tasks.
     * @return the list of tasks
     */
    suspend fun getAll(): Tasks

    /**
     * Fetch a list of pending tasks.
     * @return the list of tasks
     */
    suspend fun getPending(): Tasks

    /**
     * Fetch a task given its id.
     * @param id the id
     * @return the task
     */
    suspend fun getById(id: Long): Task

    /**
     * Fetch a list of overdue tasks.
     * @return the list of tasks
     */
    suspend fun getOverdue(): Tasks

    /**
     * Fetch a list of completed tasks.
     * @return the list of tasks
     */
    suspend fun getCompleted(): Tasks

    /**
     * Fetch a list of tags.
     * @return the list of tags
     */
    suspend fun getTags(): List<String>

    /**
     * Fetch a list of tasks that contain a tag.
     * @param tag the tag
     * @return the list of tasks
     */
    suspend fun getByTag(tag: String): Tasks

    /**
     * Update a task.
     * @param task a task with the same id as a stored task.
     */
    suspend fun update(task: Task)

    /**
     * Add a new task.
     * @param task the task
     * @return the id of the added item
     */
    suspend fun add(task: Task): Long

    /**
     * Remove a task by id.
     * @param id the id
     */
    suspend fun removeById(id: Long)

    /**
     * Remove all tasks.
     */
    suspend fun removeAll()

    /**
     * Refresh the repository.
     */
    suspend fun refresh()
}
