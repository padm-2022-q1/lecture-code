package br.edu.ufabc.listacontatosclient

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import br.edu.ufabc.listacontatosclient.model.Contact
import br.edu.ufabc.listacontatosclient.model.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository()

    val isLoading = MutableLiveData(false)

    sealed class Status {
        class Error(val e: Exception) : Status()
        object Success : Status()
    }

    data class ContactListResult(
        val result: List<Contact>?,
        val status: Status
    )

    data class ContactResult(
        val result: Contact?,
        val status: Status
    )

    data class IdResult(
        val result: Long?,
        val status: Status
    )

    fun allContacts() = liveData {
        try {
            emit(ContactListResult(repository.getAll(), Status.Success))
        } catch (e: Exception) {
            emit(ContactListResult(null,
                Status.Error(Exception("Failed to fetch list of contacts", e))))
        }
    }

    fun getById(id: Long) = liveData {
        try {
            emit(ContactResult(repository.getById(id), Status.Success))
        } catch (e: Exception) {
            emit(ContactResult(null,
                Status.Error(Exception("Failed to get item with id $id", e))))
        }
    }

    fun add(contact: Contact) = liveData {
        try {
            emit(IdResult(repository.add(contact), Status.Success))
        } catch (e: Exception) {
            emit(IdResult(null, Status.Error(Exception("Failed to add contact", e))))
        }
    }




    fun getAt(position: Int) = repository.getAt(position)


}