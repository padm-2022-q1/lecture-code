package br.edu.ufabc.todocloud.model

import java.text.SimpleDateFormat
import java.util.*

typealias Tasks = List<Task>

/**
 * A task.
 * @property id the id
 * @property title the title
 * @property deadline the deadline
 * @property completed the completion status
 * @property tags the list of tags
 */
data class Task(
    val id: Long,
    val title: String,
    val deadline: Date?,
    val completed: Boolean,
    val tags: List<String>?
) {
    /**
     * Format the deadline in shorthand notation.
     */
    fun formattedDeadline(): String = deadline?.let { formatDate(deadline) } ?: ""

    companion object {
        private val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        /**
         * Converts a string to a date.
         * @param date the string
         * @return the date
         */
        fun parseDate(date: String): Date? = format.parse(date)

        /**
         * Converts a date to a string.
         * @param date the date
         * @return the string
         */
        fun formatDate(date: Date): String = format.format(date)

        /**
         * Normalizes the time information in a date.
         * @param date the date
         * @return the date with normalized time
         */
        fun simplifyDate(date: Date): Date? = parseDate(formatDate(date))
    }
}
