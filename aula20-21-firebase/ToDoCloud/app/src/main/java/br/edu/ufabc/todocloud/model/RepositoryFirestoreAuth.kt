package br.edu.ufabc.todocloud.model

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class RepositoryFirestoreAuth(application: Application) : Repository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val isConnected = AtomicBoolean(true)

    companion object {
        private const val tasksCollection = "tasks_auth"
        private const val taskIdDoc = "taskId"

        private object TaskDoc {
            const val id = "id"
            const val title = "title"
            const val deadline = "deadline"
            const val tags = "tags"
            const val completed = "completed"
            const val user = "user"
        }
    }

    private data class TaskFirestore(
        val id: Long? = null,
        val title: String? = null,
        val deadline: Date? = null,
        val tags: List<String>? = null,
        val completed: Boolean? = null,
        val user: String? = null
    ) {
        fun toTask() = Task(
            id = id ?: 0,
            title = title ?: "",
            deadline = deadline,
            tags = tags,
            completed = completed ?: false
        )

        companion object {
            fun fromTask(task: Task, user: String) = TaskFirestore(
                id = task.id,
                title = task.title,
                deadline = task.deadline,
                tags = task.tags,
                completed = task.completed,
                user = user
            )
        }
    }

    private data class TaskId(
        val value: Long? = null
    )

    init {
        application.applicationContext.getSystemService(ConnectivityManager::class.java).apply {
            val connected = getNetworkCapabilities(activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

            isConnected.set(connected)
            registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected.set(true)
                }

                override fun onLost(network: Network) {
                    isConnected.set(false)
                }
            })
        }
    }

    private fun getSource() = if (isConnected.get()) Source.DEFAULT else Source.CACHE

    private fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid
        ?: throw Exception("No user is signed in")

    private fun getCollection() = db.collection(tasksCollection)

    override suspend fun getAll(): Tasks = getCollection()
        .whereEqualTo(TaskDoc.user, getCurrentUser())
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java).map { it.toTask() }

    override suspend fun getPending(): Tasks = getCollection()
        .whereEqualTo(TaskDoc.user, getCurrentUser())
        .whereEqualTo(TaskDoc.completed, false)
        .orderBy(TaskDoc.deadline)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .map { it.toTask() }

    override suspend fun getById(id: Long): Task = getCollection()
        .whereEqualTo(TaskDoc.user, getCurrentUser())
        .whereEqualTo(TaskDoc.id, id)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .first()
        .toTask()

    override suspend fun getOverdue(): Tasks = getCollection()
        .whereEqualTo(TaskDoc.user, getCurrentUser())
        .whereLessThan(TaskDoc.deadline, Task.simplifyDate(Date()) ?: Date())
        .orderBy(TaskDoc.deadline)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .map { it.toTask() }

    override suspend fun getCompleted(): Tasks = getCollection()
        .whereEqualTo(TaskDoc.user, getCurrentUser())
        .whereEqualTo(TaskDoc.completed, true)
        .orderBy(TaskDoc.deadline)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .map { it.toTask() }

    override suspend fun getTags(): List<String> = mutableSetOf<String>().also { set ->
        getCollection()
            .whereEqualTo(TaskDoc.user, getCurrentUser())
            .get(getSource())
            .await()
            .forEach { queryDocumentSnapshot ->
                set.addAll(queryDocumentSnapshot
                    .toObject(TaskFirestore::class.java).tags ?: emptyList())
            }
    }.toList().sorted()

    override suspend fun getByTag(tag: String): Tasks = getCollection()
        .whereEqualTo(TaskDoc.user, getCurrentUser())
        .whereArrayContains(TaskDoc.tags, tag)
        .orderBy(TaskDoc.deadline)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .map { it.toTask() }

    override suspend fun update(task: Task) {
        getCollection()
            .whereEqualTo(TaskDoc.user, getCurrentUser())
            .whereEqualTo(TaskDoc.id, task.id)
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to update element with non-existing id ${task.id}")
                querySnapshot.first().reference.set(TaskFirestore.fromTask(task, getCurrentUser()))
            }
    }

    override suspend fun add(task: Task): Long = TaskFirestore(
        id = nextId(),
        title = task.title,
        deadline = task.deadline,
        tags = task.tags,
        completed = task.completed,
        user = getCurrentUser()
    ).let {
        getCollection().add(it)
        it.id ?: throw Exception("Failed to add element with a valid id")
    }

    override suspend fun removeById(id: Long) {
        getCollection()
            .whereEqualTo(TaskDoc.user, getCurrentUser())
            .whereEqualTo(TaskDoc.id, id)
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    throw Exception("Failed to remove element with non-existing id $id")
                }
                querySnapshot.first().reference.delete()
            }
    }

    override suspend fun removeAll() {
        getCollection()
            .whereEqualTo(TaskDoc.user, getCurrentUser())
            .get(getSource())
            .await()
            .forEach { queryDocumentSnapshot ->
                queryDocumentSnapshot.reference.delete()
            }
    }

    override suspend fun refresh() {

    }

    private suspend fun nextId(): Long = getCollection()
        .document(taskIdDoc)
        .get(getSource())
        .await()
        .let { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val oldValue = documentSnapshot.toObject(TaskId::class.java)?.value
                    ?: throw Exception("Failed to retrieve previous id")
                TaskId(oldValue + 1)
            } else {
                TaskId(1)
            }.let { newTaskId ->
                documentSnapshot.reference.set(newTaskId)
                newTaskId.value ?: throw Exception("New id should not be null")
            }
        }
}