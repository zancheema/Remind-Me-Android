package com.sleekdeveloper.android.remindme.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.sleekdeveloper.android.remindme.data.Result
import com.sleekdeveloper.android.remindme.data.Result.Error
import com.sleekdeveloper.android.remindme.data.Result.Success
import com.sleekdeveloper.android.remindme.data.source.AppDataSource
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.data.source.domain.asDatabaseEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDataSource(
    database: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : AppDataSource {

    private val tasksDao = database.tasksDao()

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return tasksDao.observeTasks().map { tasks ->
            Success(tasks.map { it.asDomainModel() })
        }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            val tasks = tasksDao.getTasks().map { it.asDomainModel() }
            Success(tasks)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun getTaskWithId(id: String): Result<Task> = withContext(ioDispatcher) {
        return@withContext try {
            val task = tasksDao.getTaskById(id)
            if (task != null) Success(task.asDomainModel())
            else Error(Exception("Task not found"))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insertTask(task.asDatabaseEntity())
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        completeTask(task.id)
    }

    override suspend fun completeTask(id: String) {
        tasksDao.updateCompleted(id, true)
    }

    override suspend fun deleteTask(id: String) = withContext(ioDispatcher) {
        tasksDao.deleteTaskById(id)
    }

    override suspend fun clearCompletedTasks() = withContext(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }

    override suspend fun deleteAllTasks() {
        tasksDao.deleteTasks()
    }
}
