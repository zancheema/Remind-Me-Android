package com.sleekdeveloper.android.remindme.util

sealed class TaskRepeatOption(val id: Int, val name: String) {
    object Once : TaskRepeatOption(0, "Once")
    object Daily : TaskRepeatOption(1, "Daily")
    object Weekly : TaskRepeatOption(2, "Weekly")
    object Monthly : TaskRepeatOption(3, "Monthly")
    object Yearly : TaskRepeatOption(4, "Yearly")
}

fun getAllTaskRepeatOptions() = listOf(
    TaskRepeatOption.Once,
    TaskRepeatOption.Daily,
    TaskRepeatOption.Weekly,
    TaskRepeatOption.Monthly,
    TaskRepeatOption.Yearly
)
