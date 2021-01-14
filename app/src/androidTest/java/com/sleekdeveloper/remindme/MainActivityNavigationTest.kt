package com.sleekdeveloper.remindme

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sleekdeveloper.remindme.di.AppRepositoryModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppRepositoryModule::class)
@HiltAndroidTest
class MainActivityNavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun clickAddTaskFab_NavigatesToCreateTask() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.addTaskFab))
            .perform(click())

        onView(withId(R.id.createTaskLayout))
            .check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun createTask_NavigateBack_NavigatesToTasks() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.addTaskFab))
            .perform(click())
        onView(withId(R.id.createTaskLayout))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.tasksLayout))
            .check(matches(isDisplayed()))

        activityScenario.close()
    }
}