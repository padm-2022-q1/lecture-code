package br.edu.ufabc.todoasync.model

import java.text.SimpleDateFormat
import java.util.*

data class Task(
    val id: Long,
    val title: String,
    val deadline: Date,
    val completed: Boolean,
    val tags: List<String>
) {
    fun formattedDeadline(): String =
        SimpleDateFormat("dd/MM/yy", Locale.US).format(deadline)
}