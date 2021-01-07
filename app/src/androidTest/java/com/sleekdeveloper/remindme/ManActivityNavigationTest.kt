package com.sleekdeveloper.remindme

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class ManActivityNavigationTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickAddTaskFab_NavigatesToCreateTask() {
        onView(withId(R.id.addTaskFab))
            .perform(click())

        onView(withId(R.id.createTaskLayout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun createTask_NavigateBack_NavigatesToTasks() {
        onView(withId(R.id.addTaskFab))
            .perform(click())
        onView(withId(R.id.createTaskLayout))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.tasksLayout))
            .check(matches(isDisplayed()))
    }
}