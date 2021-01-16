package com.sleekdeveloper.remindme.createtask

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sleekdeveloper.remindme.Event
import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.util.getAllTaskRepeatOptions
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import com.sleekdeveloper.remindme.R

class CreateTaskViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _taskAddedEvent = MutableLiveData<Event<Boolean>>()
    val taskAddedEvent: LiveData<Event<Boolean>>
        get() = _taskAddedEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>>
        get() = _snackbarText

    private val _pickTimeEvent = MutableLiveData<Event<Boolean>>()
    val pickTimeEvent: LiveData<Event<Boolean>>
        get() = _pickTimeEvent

    private val _pickDateEvent = MutableLiveData<Event<Boolean>>()
    val pickDateEvent: LiveData<Event<Boolean>>
        get() = _pickDateEvent

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
        addTask(task)
    }

    private fun addTask(task: Task) {
        viewModelScope.launch {
            repository.saveTask(task)
            _taskAddedEvent.value = Event(true)
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
        (date != null).also { if (!it) showSnackbarText(SnackBarMessage.EMPTY_DATE) }

    private fun verifyTitle(title: String?) =
        (title != null && title.isNotEmpty())
            .also { valid ->
                if (!valid) showSnackbarText(SnackBarMessage.EMPTY_TITLE)
            }

    private fun showSnackbarText(msg: SnackBarMessage) {
        val resId = when (msg) {
            SnackBarMessage.EMPTY_TITLE -> R.string.empty_title_event
            SnackBarMessage.EMPTY_DATE -> R.string.empty_date_event
        }
        showSnackbarText(resId)
    }

    private fun showSnackbarText(resId: Int) {
        _snackbarText.value = Event(resId)
    }

    fun pickDate() {
        _pickDateEvent.value = Event(true)
    }

    fun pickTime() {
        _pickTimeEvent.value = Event(true)
    }

    fun setDate(date: LocalDate) {
        this.date.value = date
    }

    fun setTime(time: LocalTime) {
        this.time.value = time
    }
}

private enum class SnackBarMessage {
    EMPTY_TITLE,
    EMPTY_DATE
}
