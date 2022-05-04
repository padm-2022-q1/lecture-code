package br.edu.ufabc.todostorage.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The Room database.
 */
@Database(
    entities = [TaskRoom::class, Tag::class, TaskTag::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * The task DAO.
     */
    abstract fun taskDao(): TaskDao

    /**
     * The tag DAO.
     */
    abstract fun tagDao(): TagDao

    /**
     * The task-tag (join table) DAO.
     */
    abstract fun taskTagDao(): TaskTagDao
}
