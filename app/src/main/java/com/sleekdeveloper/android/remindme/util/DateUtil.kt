package com.sleekdeveloper.android.remindme.util

import java.time.LocalDate

fun LocalDate.isDelayed(): Boolean = compareToToday() < 0

fun LocalDate.isToday(): Boolean = compareToToday() == 0

fun LocalDate.isUpcoming(): Boolean = compareToToday() > 0

private fun LocalDate.compareToToday(): Int = compareDays(LocalDate.now())

private fun LocalDate.compareDays(other: LocalDate): Int {
    val days = year * 366 + this.dayOfYear
    val otherDays = other.year * 366 + other.dayOfYear

    return days.compareTo(otherDays)
}