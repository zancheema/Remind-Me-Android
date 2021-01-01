package com.sleekdeveloper.remindme.tasks

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sleekdeveloper.remindme.Event
import com.sleekdeveloper.remindme.data.Result.Error
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.util.isDelayed
import com.sleekdeveloper.remindme.util.isToday
import com.sleekdeveloper.remindme.util.isUpcoming

class TasksViewModel(
        repository: AppRepository
) : ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val allTasks: LiveData<List<Task>> =
            repository.observeAllTasks().map { result ->
                when (result) {
                    is Success -> {
                        setTasksLoadingEvent(false)
                        result.data
                    }
                    is Error -> {
                        setTasksLoadingEvent(true)
                        emptyList()
                    }
                    else -> emptyList()
                }
            }

    val delayedTasks: LiveData<List<Task>> =
            allTasks.map { tasks ->
                tasks.filter { it.date.isDelayed() }
            }

    val todayTasks: LiveData<List<Task>> =
            allTasks.map { tasks ->
                tasks.filter { it.date.isToday() }
            }

    val upcomingTasks: LiveData<List<Task>> =
            allTasks.map { tasks ->
                tasks.filter { it.date.isUpcoming() }
            }

    val noTasksEvent: LiveData<Event<Boolean>> =
            allTasks.map { tasks -> Event(tasks.isEmpty()) }

    private val _tasksLoadingErrorEvent = MutableLiveData<Event<Boolean>>()
    val tasksLoadingErrorEvent: LiveData<Event<Boolean>>
        get() = _tasksLoadingErrorEvent

    private val _addTaskEvent = MutableLiveData<Event<Boolean>>()
    val addTaskEvent: LiveData<Event<Boolean>>
        get() = _addTaskEvent

    fun addTask() {
        _addTaskEvent.value = Event(true)
    }

    private fun setTasksLoadingEvent(value: Boolean) {
        _tasksLoadingErrorEvent.value = Event(value)
    }
}
