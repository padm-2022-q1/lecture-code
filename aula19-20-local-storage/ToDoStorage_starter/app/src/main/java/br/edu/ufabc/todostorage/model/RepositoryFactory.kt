package br.edu.ufabc.todostorage.model

import android.app.Application

class RepositoryFactory(private val application: Application) {

    enum class Type {
        WebService
    }

    fun create(type: Type = Type.WebService) = when (type) {
        Type.WebService -> RepositoryWebService()
    }
}