package br.edu.ufabc.todostorage.model.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * The tag entity.
 * @property id the id
 * @property name the name
 */
@Entity(
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String
)
