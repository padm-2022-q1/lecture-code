package br.edu.ufabc.listacontatosresponsiva.model

/**
 * Transfer (domain) object.
 */
data class Contact(
    var name: String,
    var email: String,
    var phone: String,
    var address: String
)