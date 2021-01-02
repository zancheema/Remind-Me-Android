package com.sleekdeveloper.remindme.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object StubRepository : AppRepository {
    private val observableTasks =
        MutableLiveData<Result<List<Task>>>(Success(emptyList()))

    override fun observeAllTasks(): LiveData<Result<List<Task>>> {
        return observableTasks
    }

    override suspend fun addTask(task: Task) {
        val newList = ((observableTasks).value as Success).data
            .toMutableList()
            .apply { add(task) }
        withContext(Dispatchers.Main) {
            observableTasks.value = Success(newList)
        }
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
}
