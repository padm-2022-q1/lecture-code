package br.edu.ufabc.todostorage.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * The task DAO.
 */
@Dao
interface TaskDao {
    /**
     * Insert a new task.
     * @param taskRoom the task
     * @return the inserted id
     */
    @Insert
    fun insert(taskRoom: TaskRoom): Long

    /**
     * Delete a task given its id.
     * @param id the id
     */
    @Query("DELETE FROM taskroom WHERE id=:id")
    fun deleteById(id: Long)

    /**
     * Update a task.
     * @param taskRoom the task
     */
    @Update
    fun update(taskRoom: TaskRoom)
}
