package br.edu.ufabc.listacontatosmvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as App).repository

    val clickedItemId by lazy {
        MutableLiveData<Long?>()
    }

    fun allContacts() = repository.getAll()

    fun getAt(position: Int) = repository.getAt(position)

    fun getById(id: Long) = repository.getById(id)
}