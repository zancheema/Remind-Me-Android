package com.sleekdeveloper.android.remindme.upcomingtasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.sleekdeveloper.android.remindme.Event
import com.sleekdeveloper.android.remindme.data.Result.Error
import com.sleekdeveloper.android.remindme.data.Result.Success
import com.sleekdeveloper.android.remindme.data.source.AppRepository
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.util.isUpcoming

class UpcomingTasksViewModel @ViewModelInject constructor(
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
                        tasks.data.filter { it.date.isUpcoming() }
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
