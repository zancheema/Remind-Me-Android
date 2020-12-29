package com.sleekdeveloper.remindme.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sleekdeveloper.remindme.Event
import com.sleekdeveloper.remindme.data.Result
import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.util.isDelayed
import com.sleekdeveloper.remindme.util.isToday
import com.sleekdeveloper.remindme.util.isUpcoming

class TasksViewModel(repository: AppRepository) : ViewModel() {
    private val allTasks: LiveData<List<Task>> =
            repository.observeAllTasks().map { result ->
                when (result) {
                    is Result.Success -> result.data
                    else -> emptyList()
                }
            }

    val delayedTasks: LiveData<List<Task>> =
            allTasks.map { tasks ->
                tasks.filter { it.timestamp.isDelayed() }
            }

    val todayTasks: LiveData<List<Task>> =
            allTasks.map { tasks ->
                tasks.filter { it.timestamp.isToday() }
            }

    val upcomingTasks: LiveData<List<Task>> =
            allTasks.map { tasks ->
                tasks.filter { it.timestamp.isUpcoming() }
            }

    private val _addTaskEvent = MutableLiveData<Event<Boolean>>()
    val addTaskEvent: LiveData<Event<Boolean>>
        get() = _addTaskEvent

    fun addTask() {
        _addTaskEvent.value = Event(true)
    }
}
