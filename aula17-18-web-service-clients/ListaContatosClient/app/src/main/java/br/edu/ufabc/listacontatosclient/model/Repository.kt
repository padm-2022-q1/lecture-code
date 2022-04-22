package br.edu.ufabc.listacontatosclient.model

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.FileNotFoundException
import java.io.InputStream

class Repository {
    companion object {
        private const val url = "https://padm-2022q1-listacontatos.herokuapp.com/api"
        private const val ra = "2123689"
        private const val baseUrl = "$url/$ra/"
    }

    private data class ServiceResult (
        val items: List<Contact>?,
        val item: Contact?,
        val itemId: Long?,
        val message: String?,
        val debug: String?
    )

    private interface ContactService {

        @GET("list")
        suspend fun list(): Response<ServiceResult>

        @GET("item/{id}")
        suspend fun getById(@Path("id") id: Long): Response<ServiceResult>

        @POST("./")
        suspend fun add(@Body contact: Contact): Response<ServiceResult>
    }

    private val service = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ContactService::class.java)

    private fun deserializeErrorResult(response: Response<ServiceResult>)
            = Gson().fromJson<ServiceResult>(response.errorBody()?.charStream()?.readText(),
        object: TypeToken<ServiceResult>() {}.type)

    private fun checkResponseCodes(response: Response<ServiceResult>) = when (response.code()) {
        200 -> {}
        401 -> throw Exception("User id is not authorized")
        500 -> throw Exception("Internal server error: ${deserializeErrorResult(response).message}")
        406 -> throw Exception("Invalid input data format ${deserializeErrorResult(response).debug}")
        else -> throw Exception("Invalid response code")
    }

    suspend fun getAll(): List<Contact> = withContext(Dispatchers.IO) {
        service.list().let { response ->
            checkResponseCodes(response)
            response.body()?.items ?: throw Exception("Failed to retrieve contact list")
        }
    }

    suspend fun getById(id: Long): Contact = withContext(Dispatchers.IO) {
        service.getById(id).let { response ->
            checkResponseCodes(response)
            response.body()?.item ?: throw Exception("Failed to retrieve item by id")
        }
    }

    suspend fun add(contact: Contact) = withContext(Dispatchers.IO) {
        service.add(contact).let { response ->
            checkResponseCodes(response)
            response.body()?.itemId ?: throw Exception("Failed to insert new item")
        }
    }

    fun getAt(position: Int) {}

}