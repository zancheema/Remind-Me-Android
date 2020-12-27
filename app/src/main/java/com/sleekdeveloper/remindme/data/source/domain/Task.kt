package com.sleekdeveloper.remindme.data.source.domain

import java.sql.Timestamp

data class Task(
    val id: String,
    val title: String,
    val time: Timestamp,
    val daily: Boolean,
    val completed: Boolean,
    val important: Boolean
)