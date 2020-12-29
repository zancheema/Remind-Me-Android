package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.domain.Task

class FakeTestRepository : AppRepository {
    private val observableTasks = MutableLiveData<Result<List<Task>>>(Success(emptyList()))

    override fun observeAllTasks(): LiveData<Result<List<Task>>> =
            observableTasks

    override suspend fun addTask(task: Task) {
        val newList = ((observableTasks).value as Success).data
                .toMutableList()
                .apply { add(task) }
        observableTasks.value = Success(newList)
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }
}
