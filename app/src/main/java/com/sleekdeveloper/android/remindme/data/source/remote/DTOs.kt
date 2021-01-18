package com.sleekdeveloper.android.remindme.data.source.remote

class TaskDTO(
    var id: String,
    var title: String,
    var date: Long,
    var time: Long?,
    var repeat: Int,
    var isCompleted: Boolean,
    var isImportant: Boolean
)
