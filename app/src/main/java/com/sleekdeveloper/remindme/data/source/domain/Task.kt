package com.sleekdeveloper.remindme.data.source.domain

import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class Task(
        val title: String,
        val date: LocalDate,
        val time: LocalTime? = null,
        val id: String = UUID.randomUUID().toString(),
        val repeatsDaily: Boolean = false,
        val completed: Boolean = false,
        val important: Boolean = false
)