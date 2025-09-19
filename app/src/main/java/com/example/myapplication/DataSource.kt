package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.myapplication.model.TaskModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PreferencesDataStore (private val context: Context) {
    fun <T> getPreferences(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { preferences -> preferences[key] }
    }

    suspend fun <T> setPreferences(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit {preferences -> preferences[key] = value}
    }
}

class FirestoreDataSource {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference = firestore.collection("tarefas")
    private val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    private val _allTasks = MutableStateFlow<MutableList<TaskModel>>(mutableListOf())
    private val allTasks: StateFlow<MutableList<TaskModel>> = _allTasks

    fun saveTask(task: TaskModel){
        val documentReference = collectionReference.document()
        val docId = documentReference.id
        val payload = mapOf(
            "id" to docId,
            "title" to task.title,
            "description" to task.description,
            "priority" to task.priority,
            "location" to task.location,
            "createdAt" to now.date
        )
        documentReference
            .set(payload)
            .addOnSuccessListener{ Log.d("FIRESTORE", "Documento ${now}-${task.title} salvo com sucesso") }
            .addOnFailureListener{ Log.d("FIRESTORE", "Erro ao salvar documento ${now}-${task.title}") }
            .addOnCanceledListener{ Log.d("FIRESTORE", "A ação de salvar o documento foi cancelada") }
            .addOnCompleteListener{ Log.d("FIRESTORE", "Ação finalizada!!") }
    }

    fun getTasks(): Flow<List<TaskModel>> = callbackFlow {
        val listener = collectionReference
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    val list = snapshot?.documents?.mapNotNull {
                        it.toObject(TaskModel::class.java)?.apply { id = it.id }
                    } ?: emptyList()
                    trySend(list)
                }
            }
        awaitClose { listener.remove() }
    }

    fun deleteTask(docId: String, onResult: (Boolean) -> Unit) {
        collectionReference
            .document(docId)
            .delete()
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Documento $docId deletado com sucesso")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Erro ao deletar documento $docId", e)
                onResult(false)
            }
    }

    fun updateTask(docId: String, updates: Map<String, Any>, onResult: (Boolean) -> Unit) {
        collectionReference.document(docId)
            .update(updates)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { e -> Log.e("FIRESTORE", "Erro update", e); onResult(false) }
    }

    fun getTaskById(docId: String, onResult: (TaskModel?) -> Unit) {
        collectionReference
            .document(docId)
            .get()
            .addOnSuccessListener { snapshot ->
                val task = snapshot.toObject(TaskModel::class.java)
                onResult(task)
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Erro ao buscar tarefa $docId", e)
                onResult(null)
            }
    }

}