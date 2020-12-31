package com.sleekdeveloper.remindme.todaytasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sleekdeveloper.remindme.Event
import com.sleekdeveloper.remindme.data.Result.Error
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.util.isToday

class TodayTasksViewModel(
        repository: AppRepository
) : ViewModel() {

    private val _tasksLoadingErrorEvent = MutableLiveData<Event<Boolean>>()
    val tasksLoadingErrorEvent: LiveData<Event<Boolean>>
        get() = _tasksLoadingErrorEvent

    val tasks: LiveData<List<Task>> =
            repository.observeAllTasks().map { tasks ->
                when (tasks) {
                    is Success -> {
                        _tasksLoadingErrorEvent.value = Event(false)
                        tasks.data.filter { it.timestamp.isToday() }
                    }
                    is Error -> {
                        _tasksLoadingErrorEvent.value = Event(true)
                        emptyList()
                    }
                    else -> emptyList()
                }
            }

    val noTasksEvent: LiveData<Event<Boolean>> =
            tasks.map { Event(it.isEmpty()) }
}
