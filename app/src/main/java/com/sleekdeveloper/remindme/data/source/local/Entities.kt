package com.sleekdeveloper.remindme.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
class DbTask(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "time") var time: Long?,
    @ColumnInfo(name = "repeat") var repeat: Int,
    @ColumnInfo(name = "completed") var isCompleted: Boolean,
    @ColumnInfo(name = "important") var isImportant: Boolean
)
