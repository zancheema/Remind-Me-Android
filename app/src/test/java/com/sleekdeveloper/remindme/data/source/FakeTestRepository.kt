package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.Result.Error
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.domain.Task
import java.lang.Exception

class FakeTestRepository : AppRepository {
    private var error: Boolean = false
    private val observableTasks =
            MutableLiveData<Result<List<Task>>>(Success(emptyList()))

    override fun observeAllTasks(): LiveData<Result<List<Task>>> {
        if (error) return liveError("No tasks found")
        return observableTasks
    }

    private fun liveError(msg: String): LiveData<Result<List<Task>>> =
            MutableLiveData(Error(Exception(msg)))

    override suspend fun addTask(task: Task) {
        val newList = ((observableTasks).value as Success).data
                .toMutableList()
                .apply { add(task) }
        observableTasks.value = Success(newList)
    }

    override suspend fun addTasks(vararg tasks: Task) {
        for (t in tasks) addTask(t)
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    fun setError(error: Boolean) {
        this.error = error
    }
}
