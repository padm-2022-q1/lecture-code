package br.edu.ufabc.todocloud.model

import android.app.Application

/**
 * A repository factory.
 * @property application the application object
 */
class RepositoryFactory(private val application: Application) {

    /**
     * The types of repositories.
     */
    enum class Type {
        InMemory
    }

    /**
     * Create a new repository given its type.
     * @param type the repository type
     */
    fun create(type: Type = Type.InMemory) = when (type) {
        Type.InMemory -> RepositoryInMemory()
    }
}
