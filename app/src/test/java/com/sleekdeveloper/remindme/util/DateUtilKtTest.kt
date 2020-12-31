package com.sleekdeveloper.remindme.util

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import java.lang.System.currentTimeMillis
import java.util.*

class DateUtilKtTest {
    private val oneDayInMillis = 86400000L
    private val yesterday = Date(currentTimeMillis() - oneDayInMillis)
    private val today = Date(currentTimeMillis())
    private val tomorrow = Date(currentTimeMillis() + oneDayInMillis)

    @Test
    fun isDelayed_verifiesTheDateHasPassed() {
        assertThat(yesterday.isDelayed(), `is`(true))
        assertThat(yesterday.isToday(), `is`(false))
        assertThat(yesterday.isUpcoming(), `is`(false))
    }

    @Test
    fun isToday_verifiesTheDateIsToday() {
        assertThat(today.isDelayed(), `is`(false))
        assertThat(today.isToday(), `is`(true))
        assertThat(today.isUpcoming(), `is`(false))
    }

    @Test
    fun isUpcoming_verifiesTheDateIsUpcoming() {
        assertThat(tomorrow.isDelayed(), `is`(false))
        assertThat(tomorrow.isToday(), `is`(false))
        assertThat(tomorrow.isUpcoming(), `is`(true))
    }
}