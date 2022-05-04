package br.edu.ufabc.todostorage.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * A Repository that wraps a web service.
 */
class RepositoryWebService : Repository {

    companion object {
        private const val url = "https://padm-2022q1-todo.herokuapp.com/api"
        private const val ra = "" // replace with your RA
    }

    private data class ServiceResult(
        val items: Tasks?,
        val tags: List<String>?,
        val item: Task?,
        val insertedId: Long?,
        val message: String?,
        val debug: String?
    )

    private interface ToDoService {
        @GET("list")
        suspend fun list(): Response<ServiceResult>

        @GET("pending")
        suspend fun pending(): Response<ServiceResult>

        @GET("overdue")
        suspend fun overdue(): Response<ServiceResult>

        @GET("completed")
        suspend fun completed(): Response<ServiceResult>

        @GET("tags")
        suspend fun tags(): Response<ServiceResult>

        @GET("item/{id}")
        suspend fun getById(@Path("id") id: Long): Response<ServiceResult>

        @GET("tag/{tag}")
        suspend fun getByTag(@Path("tag") tag: String): Response<ServiceResult>

        @POST("./")
        suspend fun insert(@Body task: Task): Response<ServiceResult>

        @DELETE("{id}")
        suspend fun deleteById(@Path("id") id: Long): Response<ServiceResult>

        @PUT("./")
        suspend fun update(@Body task: Task): Response<ServiceResult>
    }

    private val service = Retrofit.Builder()
        .baseUrl("$url/$ra/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("MM/dd/yyyy").create()))
        .build()
        .create(ToDoService::class.java)

    private fun deserializeErrorResult(response: Response<ServiceResult>)
        = Gson().fromJson<ServiceResult>(response.errorBody()?.charStream()?.readText(),
            object: TypeToken<ServiceResult>() {}.type)

    private fun checkResponseCodes(response: Response<ServiceResult>) = when(response.code()) {
        200 -> Unit
        401 -> throw Exception("User id is not authorized")
        500 -> throw Exception("Internal server error: ${deserializeErrorResult(response).message}")
        406 -> throw Exception("Invalid input data format: ${deserializeErrorResult(response).debug}")
        else -> throw Exception("Invalid response code")
    }

    /**
     * Fetch a list of all tasks.
     * @return the list of tasks
     */
    suspend fun getAll(): Tasks = withContext(Dispatchers.IO) {
        service.list().let { response ->
            checkResponseCodes(response)
            response.body()?.items ?: throw Exception("Failed to retrieve all tasks")
        }
    }

    /**
     * Fetch a list of pending tasks.
     * @return the list of tasks
     */
    override suspend fun getPending(): Tasks = withContext(Dispatchers.IO) {
        service.pending().let { response ->
            checkResponseCodes(response)
            response.body()?.items ?: throw Exception("Failed to retrieve tasks")
        }
    }

    /**
     * Fetch a task given its id.
     * @param id the id
     * @return the task
     */
    override suspend fun getById(id: Long): Task = withContext(Dispatchers.IO) {
        service.getById(id).let { response ->
            checkResponseCodes(response)
            response.body()?.item ?: throw Exception("Failed to retrieve task by id")
        }
    }

    /**
     * Fetch a list of overdue tasks.
     * @return the list of tasks
     */
    override suspend fun getOverdue(): List<Task> = withContext(Dispatchers.IO) {
        service.overdue().let { response ->
            checkResponseCodes(response)
            response.body()?.items ?: throw Exception("Failed to retrieve tasks")
        }
    }

    /**
     * Fetch a list of completed tasks.
     * @return the list of tasks
     */
    override suspend fun getCompleted(): List<Task> = withContext(Dispatchers.IO) {
        service.completed().let { response ->
            checkResponseCodes(response)
            response.body()?.items ?: throw Exception("Failed to retrieve tasks")
        }
    }

    /**
     * Fetch a list of tags.
     * @return the list of tags
     */
    override suspend fun getTags(): List<String> = withContext(Dispatchers.IO) {
        service.tags().let { response ->
            checkResponseCodes(response)
            response.body()?.tags ?: throw Exception("Failed to retrieve tags")
        }
    }

    /**
     * Fetch a list of tasks that contain a tag.
     * @param tag the tag
     * @return the list of tasks
     */
    override suspend fun getByTag(tag: String): Tasks = withContext(Dispatchers.IO) {
        service.getByTag(tag).let { response ->
            checkResponseCodes(response)
            response.body()?.items ?: throw Exception("Failed to retrieve tasks with the given tag")
        }
    }

    /**
     * Update a task.
     * @param task a task with the same id as a stored task.
     */
    override suspend fun update(task: Task) = withContext(Dispatchers.IO) {
        checkResponseCodes(service.update(task))
    }

    override suspend fun add(task: Task): Long = withContext(Dispatchers.IO) {
        service.insert(task).let { response ->
            checkResponseCodes(response)
            response.body()?.insertedId ?: throw Exception("Failed to insert item")
        }
    }

    override suspend fun removeById(id: Long) = withContext(Dispatchers.IO) {
        checkResponseCodes(service.deleteById(id))
    }

    override suspend fun refresh() { }
}
