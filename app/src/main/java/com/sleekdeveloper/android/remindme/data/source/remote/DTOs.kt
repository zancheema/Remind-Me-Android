package com.sleekdeveloper.android.remindme.data.source.remote

import com.google.gson.annotations.SerializedName

data class TaskDTO(
    @SerializedName("_id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("date") var date: Long,
    @SerializedName("time") var time: Long?,
    @SerializedName("repeat") var repeat: Int,
    @SerializedName("isCompleted") var isCompleted: Boolean,
    @SerializedName("isImportant") var isImportant: Boolean
)
