package com.sleekdeveloper.android.remindme.data.source.remote

import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.util.getAllTaskRepeatOptions
import java.time.LocalDate
import java.time.LocalTime

fun TaskDTO.asDomainModel() = Task(
    title = title,
    date = LocalDate.ofEpochDay(date),
    time = time?.let { LocalTime.ofNanoOfDay(it) },
    id = id,
    repeat = getAllTaskRepeatOptions()[repeat],
    isCompleted = isCompleted,
    isImportant = isImportant
)