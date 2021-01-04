package com.sleekdeveloper.remindme.createtask

import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sleekdeveloper.remindme.R
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class CreateTaskFragmentTest {
    @Test
    fun clickSetTaskDate_ShowsDatePicker() {
        launchFragmentInContainer<CreateTaskFragment>(Bundle(), R.style.Theme_RemindMe)

        onView(withId(R.id.setTaskDate))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(instanceOf(DatePicker::class.java))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickSetTaskTime_ShowsTimePicker() {
        launchFragmentInContainer<CreateTaskFragment>(Bundle(), R.style.Theme_RemindMe)

        onView(withId(R.id.setTaskTime))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(instanceOf(TimePicker::class.java))
            .check(matches(isDisplayed()))
    }

    @Test
    fun saveTaskWithEmptyTitle_DisplaysEmptyTitleMessage() {
        launchFragmentInContainer<CreateTaskFragment>(Bundle(), R.style.Theme_RemindMe)
        val context = getApplicationContext<Context>()

        onView(withId(R.id.saveTaskButton))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText(context.getString(R.string.empty_title_event)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun saveTaskWithEmptyDate_DisplaysEmptyDateMessage() {
        launchFragmentInContainer<CreateTaskFragment>(Bundle(), R.style.Theme_RemindMe)
        val context = getApplicationContext<Context>()

        onView(withId(R.id.editTaskTitle))
            .check(matches(isDisplayed()))
            .perform(typeText("TITLE"), closeSoftKeyboard())
        onView(withId(R.id.saveTaskButton))
            .perform(click())
        onView(withText(context.getString(R.string.empty_date_event)))
            .check(matches(isDisplayed()))
    }
}