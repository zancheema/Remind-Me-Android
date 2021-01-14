package com.sleekdeveloper.remindme

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.PickerActions.setTime
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sleekdeveloper.remindme.di.AppRepositoryModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@LargeTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppRepositoryModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun createTask_savesNewTaskInTheRepository() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        val title = "Example Task"
        val date = LocalDate.of(2050, 2, 3)
        val time = LocalTime.of(14, 26)

        onView(withId(R.id.addTaskFab))
            .check(matches(isDisplayed()))
            .perform(click())

        // set title
        onView(withId(R.id.editTaskTitle))
            .perform(typeText(title), closeSoftKeyboard())
        // set date
        onView(withId(R.id.setTaskDate))
            .perform(click())
        onView(instanceOf(DatePicker::class.java))
            .perform(setDate(date.year, date.monthValue, date.dayOfMonth))
        onView(withText("OK")).perform(click())

        // set time
        onView(withId(R.id.setTaskTime))
            .perform(click())
        onView(instanceOf(TimePicker::class.java))
            .perform(setTime(time.hour, time.minute))
        onView(withText("OK")).perform(click())

        // save
        onView(withId(R.id.saveTaskButton))
            .perform(click())

        // check if the task is saved as upcoming task
        onView(withId(R.id.upcomingTasksList))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        hasDescendant(withText(title)),
                    )
                )
            )
        onView(withId(R.id.upcomingTasksList))
            .check(
                matches(hasDescendant(withText(date.toString())))
            )
        onView(withId(R.id.upcomingTasksList))
            .check(
                matches(hasDescendant(withText(time.toString())))
            )
        onView(withId(R.id.upcomingTasksList))
            .check(
                matches(hasDescendant(withText("Once")))
            )

        activityScenario.close()
    }
}