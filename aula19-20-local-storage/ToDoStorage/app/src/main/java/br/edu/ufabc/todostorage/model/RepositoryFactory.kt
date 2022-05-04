package br.edu.ufabc.todostorage.model

import android.app.Application
import br.edu.ufabc.todostorage.model.room.RepositoryRoom

/**
 * A repository factory.
 * @property application the application object
 */
class RepositoryFactory(private val application: Application) {

    /**
     * The types of repositories.
     */
    enum class Type {
        WebService,
        InMemory,
        Cached,
        File,
        Room
    }

    /**
     * Create a new repository given its type.
     * @param type the repository type
     */
    fun create(type: Type = Type.Cached) = when (type) {
        Type.WebService -> RepositoryWebService()
        Type.InMemory -> RepositoryInMemory(false)
        Type.Cached -> RepositoryCached(application)
        Type.File -> RepositoryFile(application)
        Type.Room -> RepositoryRoom(application)
    }
}
