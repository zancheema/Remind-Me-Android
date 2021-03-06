package com.sleekdeveloper.android.remindme.tasks

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sleekdeveloper.android.remindme.R
import com.sleekdeveloper.android.remindme.data.source.AppRepository
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.di.AppRepositoryModule
import com.sleekdeveloper.android.remindme.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import javax.inject.Inject

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@UninstallModules(AppRepositoryModule::class)
@HiltAndroidTest
class TasksFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    private val delayedTask1 = Task("dt_1", LocalDate.of(2005, 2, 3))
    private val delayedTask2 = Task("dt_2", LocalDate.of(2015, 3, 3))
    private val todayTask = Task("todayTask", LocalDate.now())
    private val upcomingTask1 = Task("up_1", LocalDate.of(2050, 3, 4))
    private val upcomingTask2 = Task("up_2", LocalDate.of(2078, 5, 4))

    @Before
    fun initWithFragment() {
        hiltRule.inject()
        runBlocking {
            repository.saveTasks(
                delayedTask1,
                delayedTask2,
                todayTask,
                upcomingTask1,
                upcomingTask2
            )
        }
        launchFragmentInHiltContainer<TasksFragment>(Bundle(), R.style.Theme_RemindMe)
    }

    @Test
    fun delayedTasksListDisplaysDelayedTasks() {
        onView(withId(R.id.delayedTasksList))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        hasDescendant(withText(delayedTask1.title)),
                        hasDescendant(withText(delayedTask2.title)),
                        not(hasDescendant(withText(todayTask.title))),
                        not(hasDescendant(withText(upcomingTask1.title))),
                        not(hasDescendant(withText(upcomingTask2.title)))
                    )
                )
            )
    }

    @Test
    fun todayTasksListDisplaysTodayTasks() {
        onView(withId(R.id.todayTasksList))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        hasDescendant(withText(todayTask.title)),
                        not(hasDescendant(withText(delayedTask2.title))),
                        not(hasDescendant(withText(delayedTask1.title))),
                        not(hasDescendant(withText(upcomingTask1.title))),
                        not(hasDescendant(withText(upcomingTask2.title)))
                    )
                )
            )
    }

    @Test
    fun upcomingTasksListDisplaysUpcomingTasks() {
        onView(withId(R.id.upcomingTasksList))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        hasDescendant(withText(upcomingTask1.title)),
                        hasDescendant(withText(upcomingTask2.title)),
                        not(hasDescendant(withText(delayedTask1.title))),
                        not(hasDescendant(withText(delayedTask2.title))),
                        not(hasDescendant(withText(todayTask.title)))
                    )
                )
            )
    }
}