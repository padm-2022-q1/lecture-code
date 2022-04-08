package br.edu.ufabc.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.edu.ufabc.todo.model.Repository


class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val tasksFile = "tasks.json"
    }

    private val repository = Repository()

    init {
        application.resources.assets.open(tasksFile).use {
            repository.loadData(it)
        }
    }

    fun getAll() = repository.getAll()

    fun getOverdue() = repository.getOverdue()

    fun getPending() = repository.getPending()

    fun getById(id: Long) = repository.getById(id)

    fun getTags() = repository.getTags()

    fun getByTag(tag: String) = repository.getByTag(tag)

    fun getCompleted() = repository.getCompleted()
}