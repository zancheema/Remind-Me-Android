package com.sleekdeveloper.remindme.data.source.domain

import com.sleekdeveloper.remindme.util.TaskRepeatOption
import com.sleekdeveloper.remindme.util.TaskRepeatOption.Once
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID.randomUUID

data class Task(
    val title: String,
    val date: LocalDate,
    val time: LocalTime? = null,
    val id: String = randomUUID().toString(),
    val repeat: TaskRepeatOption = Once,
    var isCompleted: Boolean = false,
    val isImportant: Boolean = false
)