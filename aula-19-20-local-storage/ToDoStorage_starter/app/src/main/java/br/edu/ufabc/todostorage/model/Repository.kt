package br.edu.ufabc.todostorage.model

interface Repository {
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

    suspend fun add(task: Task): Long

    suspend fun removeById(id: Long)

    suspend fun refresh()
}