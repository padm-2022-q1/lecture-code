package br.edu.ufabc.listacontatosresponsiva

import android.app.Application
import br.edu.ufabc.listacontatosresponsiva.model.Repository

class App : Application() {
    companion object {
        private var contactsFile = "contacts.json"
    }

    val repository = Repository()

    override fun onCreate() {
        super.onCreate()

        resources.assets.open(contactsFile).use {
            repository.loadData(it)
        }
    }
}