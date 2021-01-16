package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.Result.Error
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.domain.Task
import kotlinx.coroutines.*

class DefaultAppRepository(
    private val remoteDataSource: AppDataSource,
    private val localDataSource: AppDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository {

    override fun observeAllTasks(): LiveData<Result<List<Task>>> {
        return localDataSource.observeTasks()
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if (forceUpdate) {
            try {
                updateTasksFromRemoteDataSource()
            } catch (ex: Exception) {
                return Error(ex)
            }
        }
        return localDataSource.getTasks()
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        val remoteTasks = remoteDataSource.getTasks()

        if (remoteTasks is Success) {
            localDataSource.deleteAllTasks()
            remoteTasks.data.forEach { task ->
                localDataSource.saveTask(task)
            }
        } else if (remoteTasks is Error) {
            throw remoteTasks.exception
        }
    }

    override suspend fun saveTask(task: Task) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.saveTask(task) }
            launch { localDataSource.saveTask(task) }
        }
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskWithId(id: String): Result<Task> {
        return localDataSource.getTaskWithId(id)
    }

    override suspend fun completeTask(id: String) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.completeTask(id) }
            launch { remoteDataSource.completeTask(id) }
        }
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.clearCompletedTasks() }
            launch { localDataSource.clearCompletedTasks() }
        }
    }

    override suspend fun deleteAllTasks() = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.deleteAllTasks() }
            launch { localDataSource.deleteAllTasks() }
        }
    }

    override suspend fun deleteTask(id: String) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { remoteDataSource.deleteTask(id) }
            launch { localDataSource.deleteTask(id) }
        }
    }

    override suspend fun refreshTasks() {
        TODO("Not yet implemented")
    }
}
