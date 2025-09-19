package com.example.myapplication.repository

import com.example.myapplication.FirestoreDataSource
import com.example.myapplication.model.TaskModel
import kotlinx.coroutines.flow.Flow

class TaskRepository {
    private val firestoreDataSource: FirestoreDataSource = FirestoreDataSource()

    fun saveTask(task: TaskModel) {
        firestoreDataSource.saveTask(task)
    }

    fun getTasks(): Flow<List<TaskModel>> {
        return firestoreDataSource.getTasks()
    }

    fun deleteTask(docId: String, onResult: (Boolean) -> Unit) {
        firestoreDataSource.deleteTask(docId, onResult)
    }

    fun updateTask(docId: String, updates: Map<String, Any>, onResult: (Boolean) -> Unit) {
        firestoreDataSource.updateTask(docId, updates, onResult)
    }

    fun getTaskById(docId: String, onResult: (TaskModel?) -> Unit) {
        firestoreDataSource.getTaskById(docId, onResult)
    }
}