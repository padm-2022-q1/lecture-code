package br.edu.ufabc.todostorage.model.room

import androidx.room.*

/**
 * The TaskTag (join table) DAO.
 */
@Dao
interface TaskTagDao {
    /**
     * Get a list of all tasks with the correspoding tags.
     * @return the list of tasks with tags
     */
    @Transaction
    @Query("SELECT * FROM taskroom")
    fun getAll(): List<TaskWithTags>

    /**
     * Get a list of all pending tasks.
     * @return the list of task tasks with tags
     */
    @Transaction
    @Query("SELECT * FROM taskroom WHERE completed=0")
    fun getPending(): List<TaskWithTags>

    /**
     * Get a task by its id.
     * @return the task with corresponding tags.
     */
    @Transaction
    @Query("SELECT * FROM taskroom WHERE id=:id")
    fun getById(id: Long): TaskWithTags

    /**
     * Get a list of overdue tasks.
     * @param now the current date
     * @return the list of tasks with tags
     */
    @Transaction
    @Query("SELECT * FROM taskroom WHERE completed = 0 AND deadline < :now")
    fun getOverdue(now: Long): List<TaskWithTags>

    /**
     * Get a list of completed tasks.
     * @return the list of tasks with tags.
     */
    @Transaction
    @Query("SELECT * FROM taskroom WHERE completed = 1")
    fun getCompleted(): List<TaskWithTags>

    /**
     * Get all the tasks for a given task.
     * @param tag the tag name
     * @return a tag with a list of tasks
     */
    @Transaction
    @Query("SELECT * FROM tag WHERE name = :tag")
    fun getByTag(tag: String): TagWithTasks

    /**
     * Insert a pair of task and tag.
     * @param taskTag the pair of task and tag.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(taskTag: TaskTag)
}
