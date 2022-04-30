package br.edu.ufabc.todostorage.model.room

import androidx.room.*

/**
 * The tag DAO.
 */
@Dao
interface TagDao {
    /**
     * Insert a new tag.
     * @param tag the tag
     * @return the inserted id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tag: Tag): Long

    /**
     * Delete a tag given its id.
     * @param id the id
     */
    @Query("DELETE FROM tag WHERE id=:id")
    fun deleteById(id: Long)

    /**
     * Delete a tag.
     * @param tag the tag
     */
    @Delete
    fun delete(tag: Tag)

    /**
     * List of all tags.
     * @return the list of tags
     */
    @Query("SELECT * FROM tag")
    fun list(): List<Tag>

    /**
     * Get a tag given its name.
     * @return the tag
     */
    @Query("SELECT * FROM tag WHERE name = :name")
    fun getByName(name: String): Tag
}
