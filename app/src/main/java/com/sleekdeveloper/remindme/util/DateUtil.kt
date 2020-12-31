package com.sleekdeveloper.remindme.util

import java.util.*

fun Date.isDelayed(): Boolean = compareToToday() < 0

fun Date.isToday(): Boolean = compareToToday() == 0

fun Date.isUpcoming(): Boolean = compareToToday() > 0

private fun Date.compareToToday(): Int = compareDays(Date(System.currentTimeMillis()))

private fun Date.compareDays(other: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val otherCalendar = Calendar.getInstance()
    otherCalendar.time = other

    val days = calendar.get(Calendar.YEAR) * 366 + calendar.get(Calendar.DAY_OF_YEAR)
    val otherDays = otherCalendar.get(Calendar.YEAR) * 366 + otherCalendar.get(Calendar.DAY_OF_YEAR)

    return days.compareTo(otherDays)
}