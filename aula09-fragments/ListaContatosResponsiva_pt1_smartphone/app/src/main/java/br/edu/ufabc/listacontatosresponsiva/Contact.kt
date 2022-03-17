package br.edu.ufabc.listacontatosresponsiva

/**
 * Transfer (domain) object.
 */
data class Contact(
    var name: String,
    var email: String,
    var phone: String,
    var address: String
)