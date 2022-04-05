package br.edu.ufabc.listacontatosslidingpane.model

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.FileNotFoundException
import java.io.InputStream

class Repository {
    private lateinit var contacts: List<Contact>

    fun loadData(inputStream: InputStream) {
        try {
            contacts = Klaxon().parseArray(inputStream) ?: emptyList()
        } catch (e: FileNotFoundException) {
            throw Exception("Failed to open dataset file", e)
        } catch (e: KlaxonException) {
            throw Exception("Failed to parse dataset file", e)
        }
    }

    fun getAll() = if (this::contacts.isInitialized) contacts else
        throw Exception("No data has been fetched yet")

    fun getAt(position: Int) = contacts[position]

    fun getById(id: Long) = contacts.find {
        it.id == id
    }
}