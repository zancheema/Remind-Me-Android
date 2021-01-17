package com.sleekdeveloper.android.remindme.data.source

import androidx.lifecycle.LiveData
import com.sleekdeveloper.android.remindme.data.Result
import com.sleekdeveloper.android.remindme.data.source.domain.Task

interface AppRepository {
    fun observeAllTasks(): LiveData<Result<List<Task>>>

    suspend fun getTasks(forceUpdate: Boolean = false): Result<List<Task>>

    suspend fun getTaskWithId(id: String): Result<Task>

    suspend fun refreshTasks()

    suspend fun saveTask(task: Task)

    suspend fun saveTasks(vararg tasks: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(id: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(id: String)
}