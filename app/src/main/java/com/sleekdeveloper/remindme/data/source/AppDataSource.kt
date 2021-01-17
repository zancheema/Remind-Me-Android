package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.source.domain.Task

interface AppDataSource {
    fun observeTasks(): LiveData<Result<List<Task>>>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getTaskWithId(id: String): Result<Task>

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(id: String)

    suspend fun deleteTask(id: String)

    suspend fun deleteAllTasks()

    suspend fun clearCompletedTasks()
}