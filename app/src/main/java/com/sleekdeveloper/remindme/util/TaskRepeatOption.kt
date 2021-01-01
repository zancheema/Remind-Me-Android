package com.sleekdeveloper.remindme.util

sealed class TaskRepeatOption(val name: String, val repeatsDaily: Boolean) {
    object Once : TaskRepeatOption("Once", false)
    object Daily : TaskRepeatOption("Daily", true)

    companion object {
        fun getAllTaskRepeatOptions() = listOf(
                Once,
                Daily
        )
    }
}
