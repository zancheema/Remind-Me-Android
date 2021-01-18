package com.sleekdeveloper.android.remindme.data.source.domain

import com.sleekdeveloper.android.remindme.data.source.local.DbTask
import com.sleekdeveloper.android.remindme.data.source.remote.TaskDTO

fun Task.asDatabaseEntity() = DbTask(
    id = id,
    title = title,
    date = date.toEpochDay(),
    repeat = repeat.id,
    time = time?.toNanoOfDay(),
    isCompleted = isCompleted,
    isImportant = isImportant
)

fun Task.asDataTransferObject() = TaskDTO(
    id = id,
    title = title,
    date = date.toEpochDay(),
    repeat = repeat.id,
    time = time?.toNanoOfDay(),
    isCompleted = isCompleted,
    isImportant = isImportant
)