package com.sleekdeveloper.remindme.data.source.domain

import java.sql.Timestamp

data class Task(
    val id: String,
    val title: String,
    val time: Timestamp,
    val completed: Boolean,
    val favorite: Boolean,
    val daily: Boolean
)