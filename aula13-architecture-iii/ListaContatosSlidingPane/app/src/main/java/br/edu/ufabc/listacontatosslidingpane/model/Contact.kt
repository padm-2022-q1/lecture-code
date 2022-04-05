package br.edu.ufabc.listacontatosslidingpane.model

/**
 * Transfer (domain) object.
 */
data class Contact(
    var id: Long,
    var name: String,
    var email: String,
    var phone: String,
    var address: String
)