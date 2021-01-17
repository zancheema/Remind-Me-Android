package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.Result.Error
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class FakeTestRepository @Inject constructor() : AppRepository {
    private var error: Boolean = false
    private val observableTasks =
        MutableLiveData<Result<List<Task>>>(Success(emptyList()))

    override fun observeAllTasks(): LiveData<Result<List<Task>>> {
        if (error) return liveError("No tasks found")
        return observableTasks
    }

    override suspend fun completeTask(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskWithId(id: String): Result<Task> {
        TODO("Not yet implemented")
    }

    private fun liveError(msg: String): LiveData<Result<List<Task>>> =
        MutableLiveData(Error(Exception(msg)))

    override suspend fun saveTask(task: Task) {
        val newList = ((observableTasks).value as Success).data
            .toMutableList()
            .apply { add(task) }
        withContext(Dispatchers.Main) {
            observableTasks.value = Success(newList)
        }
    }

    override suspend fun saveTasks(vararg tasks: Task) {
        for (t in tasks) saveTask(t)
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTasks() {
        TODO("Not yet implemented")
    }

    fun setError(error: Boolean) {
        this.error = error
    }
}
