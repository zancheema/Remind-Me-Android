package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.source.domain.Task

interface AppRepository {
    fun observeAllTasks(): LiveData<Result<List<Task>>>

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}