package com.sleekdeveloper.remindme.createtask

import androidx.lifecycle.*
import com.sleekdeveloper.remindme.Event
import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.util.getAllTaskRepeatOptions
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class CreateTaskViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val time = MutableLiveData<LocalTime>()

    private val date = MutableLiveData<LocalDate>()
    val selectedOptionIndex = MutableLiveData(0)

    private val taskRepeatOptions = MutableLiveData(getAllTaskRepeatOptions())

    val taskRepeatOptionNames: LiveData<List<String>>
        get() = taskRepeatOptions.map { options ->
            val names = mutableListOf<String>()
            for (o in options) names.add(o.name)
            names
        }

    val title = MutableLiveData<String>()

    private val _emptyTitleEvent = MutableLiveData<Event<Boolean>>()
    val emptyTitleEvent: LiveData<Event<Boolean>>
        get() = _emptyTitleEvent

    private val _emptyDateEvent = MutableLiveData<Event<Boolean>>()
    val emptyDateEvent: LiveData<Event<Boolean>>
        get() = _emptyDateEvent

    fun createTask() {
        val taskTitle = title.value
        val repeatsDaily = taskRepeatsDaily()
        val taskDate = date.value
        val taskTime = time.value

        // verifications
        if (verifyTaskFields(taskTitle, taskDate)) return

        val task = Task(
            taskTitle as String,
            taskDate as LocalDate,
            taskTime,
            repeat = repeatsDaily
        )

        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    private fun taskRepeatsDaily() =
        taskRepeatOptions.value!![selectedOptionIndex.value!!]

    private fun verifyTaskFields(
        taskTitle: String?,
        taskDate: LocalDate?,
    ) = !verifyTitle(taskTitle) ||
            !verifyDate(taskDate)

    private fun verifyDate(date: LocalDate?) =
        (date != null).also { if (!it) generateEmptyDateEvent() }

    private fun verifyTitle(title: String?) =
        (title != null && title.isNotEmpty())
            .also { valid ->
                if (!valid) generateEmptyTitleEvent()
            }

    private fun generateEmptyTitleEvent() {
        _emptyTitleEvent.value = Event(true)
    }

    private fun generateEmptyDateEvent() {
        _emptyDateEvent.value = Event(true)
    }

    fun setDate(date: LocalDate) {
        this.date.value = date
    }

    fun setTime(time: LocalTime) {
        this.time.value = time
    }
}
