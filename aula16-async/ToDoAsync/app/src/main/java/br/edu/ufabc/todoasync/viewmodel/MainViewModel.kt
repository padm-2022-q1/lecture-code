package br.edu.ufabc.todoasync.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ufabc.todoasync.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val tasksFile = "tasks_huge.json"
    }

    private val repository = Repository()

    val isDataReady = MutableLiveData(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            application.resources.assets.open(tasksFile).use {
                repository.loadData(it)
            }
            isDataReady.postValue(true)
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