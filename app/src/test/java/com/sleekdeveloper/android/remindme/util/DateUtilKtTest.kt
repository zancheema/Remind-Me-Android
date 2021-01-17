package com.sleekdeveloper.android.remindme.util

import com.sleekdeveloper.android.remindme.util.isDelayed
import com.sleekdeveloper.android.remindme.util.isToday
import com.sleekdeveloper.android.remindme.util.isUpcoming
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

class DateUtilKtTest {
    private val today = LocalDate.now()
    private val yesterday = LocalDate.of(1999, 2, 3)
    private val tomorrow = LocalDate.of(5000, 4, 2)

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