package br.edu.ufabc.todostorage.model.room

import androidx.room.TypeConverter
import java.util.*

/**
 * Converters for Room database.
 */
class Converters {
    /**
     * Convert a date to a timestamp.
     * @param date the date
     * @return the timestamp
     */
    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    /**
     * Convert a timestamp to a date.
     * @param timestamp the timestamp
     * @return the date
     */
    @TypeConverter
    fun timestampToDate(timestamp: Long): Date = Date(timestamp)
}
