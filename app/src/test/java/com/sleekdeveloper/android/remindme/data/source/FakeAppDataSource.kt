package com.sleekdeveloper.android.remindme.data.source

import androidx.lifecycle.LiveData
import com.sleekdeveloper.android.remindme.data.Result
import com.sleekdeveloper.android.remindme.data.Result.Error
import com.sleekdeveloper.android.remindme.data.Result.Success
import com.sleekdeveloper.android.remindme.data.source.domain.Task

class FakeAppDataSource(
    var tasks: MutableList<Task>? = mutableListOf()
) : AppDataSource {

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        tasks?.let { return Success(it.toList()) }
        return Error(
            Exception("Tasks not found")
        )
    }

    override suspend fun getTaskWithId(id: String): Result<Task> {
        tasks?.firstOrNull { it.id == id }?.let { return Success(it) }
        TODO("not implemented yet")
    }

    override suspend fun clearCompletedTasks() {
        tasks?.removeIf { it.isCompleted }
    }

    override suspend fun completeTask(task: Task) {
        completeTask(task.id)
    }

    override suspend fun completeTask(id: String) {
        tasks?.firstOrNull { it.id == id }?.let { it.isCompleted = true }
    }

    override suspend fun saveTask(task: Task) {
        tasks?.add(task)
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

    override suspend fun deleteTask(id: String) {
        tasks?.removeIf { it.id == id }
    }
}
