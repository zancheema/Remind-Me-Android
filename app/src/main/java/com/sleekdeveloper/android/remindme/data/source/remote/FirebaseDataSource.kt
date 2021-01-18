package com.sleekdeveloper.android.remindme.data.source.remote

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sleekdeveloper.android.remindme.data.Result
import com.sleekdeveloper.android.remindme.data.Result.Error
import com.sleekdeveloper.android.remindme.data.Result.Success
import com.sleekdeveloper.android.remindme.data.source.AppDataSource
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.data.source.domain.asDataTransferObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseDataSource(
    db: FirebaseFirestore = Firebase.firestore,
    private val ioDispatcer: CoroutineDispatcher = Dispatchers.IO
) : AppDataSource {

    private val tasksCollection = db.collection("tasks")

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return withContext(ioDispatcer) {
            try {
                val tasks = tasksCollection.get().await()
                    .toObjects(TaskDTO::class.java)
                    .map { it.asDomainModel() }
                Success(tasks)
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    override suspend fun getTaskWithId(id: String): Result<Task> {
        return withContext(ioDispatcer) {
            try {
                val task = tasksCollection.document(id).get().await()
                    .toObject(TaskDTO::class.java)
                    ?: return@withContext Error(Exception("Task with requested id not found"))
                Success(task.asDomainModel())
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    override suspend fun saveTask(task: Task) = withContext<Unit>(ioDispatcer) {
        try {
            tasksCollection.document(task.id)
                .set(task.asDataTransferObject())
                .await()
        } catch (e: Exception) {
            Timber.w("Failure saving task: $e")
        }
    }

    override suspend fun completeTask(task: Task) {
        completeTask(task.id)
    }

    override suspend fun completeTask(id: String) = withContext<Unit>(ioDispatcer) {
        try {
            tasksCollection.document(id)
                .update("isCompleted", true)
                .await()
        } catch (e: Exception) {
            Timber.w("Failure completing task: $e")
        }
    }

    override suspend fun deleteTask(id: String) = withContext<Unit>(ioDispatcer) {
        try {
            tasksCollection.document(id)
                .delete()
                .await()
        } catch (e: Exception) {
            Timber.w("Failure deleting task: $e")
        }
    }

    override suspend fun deleteAllTasks() = withContext<Unit>(ioDispatcer) {
        try {
            tasksCollection.document()
                .delete()
                .await()
        } catch (e: Exception) {
            Timber.w("Failure deleting all tasks: $e")
        }
    }

    override suspend fun clearCompletedTasks() = withContext(ioDispatcer) {
        try {
            tasksCollection.get().await()
                .toObjects(TaskDTO::class.java)
                .filter { it.isCompleted }
                .forEach { deleteTask(it.id) }
        } catch (e: Exception) {
            Timber.w("Failure clearing completed task: $e")
        }
    }
}