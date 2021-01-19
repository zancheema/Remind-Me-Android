package com.sleekdeveloper.android.remindme.data.source.remote

import androidx.lifecycle.LiveData
import com.sleekdeveloper.android.remindme.data.Result
import com.sleekdeveloper.android.remindme.data.source.AppDataSource
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.data.source.domain.asDataTransferObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class RemoteDataSource(
    private val service: RestApiService,
    private val ioDispatcher: CoroutineDispatcher
) : AppDataSource {
    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return withContext(ioDispatcher) {
            try {
                val tasks = service.getTasks().map { it.asDomainModel() }
                Result.Success(tasks)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getTaskWithId(id: String): Result<Task> {
        return withContext(ioDispatcher) {
            try {
                val task = service.getTaskById(id).asDomainModel()
                Result.Success(task)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        try {
            service.saveTask(task.asDataTransferObject())
        } catch (e: Exception) {
            Timber.e("Error saving task: ${e.message}")
        }
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        try {
            service.completeTask(task.id)
        } catch (e: Exception) {
            Timber.e("Error completing task: ${e.message}")
        }
    }

    override suspend fun completeTask(id: String) = withContext(ioDispatcher) {
        try {
            service.completeTask(id)
        } catch (e: Exception) {
            Timber.e("Error completing task: ${e.message}")
        }
    }

    override suspend fun deleteTask(id: String) = withContext(ioDispatcher) {
        try {
            service.deleteTask(id)
        } catch (e: Exception) {
            Timber.e("Error deleting task: ${e.message}")
        }
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        try {
            service.deleteTasks()
        } catch (e: Exception) {
            Timber.e("Error completing tasks: ${e.message}")
        }
    }

    override suspend fun clearCompletedTasks() = withContext(ioDispatcher) {
        try {
            service.clearCompletedTasks()
        } catch (e: Exception) {
            Timber.e("Error completing tasks: ${e.message}")
        }
    }
}