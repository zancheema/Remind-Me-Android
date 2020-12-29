package com.sleekdeveloper.remindme.data.source.domain

import java.sql.Timestamp

data class Task(
    val id: String,
    val title: String,
    val timestamp: Timestamp,
    val daily: Boolean = false,
    val completed: Boolean = false,
    val important: Boolean = false
)