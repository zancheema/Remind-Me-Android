package com.sleekdeveloper.remindme.upcomingtasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sleekdeveloper.remindme.R
import com.sleekdeveloper.remindme.data.source.StubRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class UpcomingTasksFragmentTest {
    @Test
    fun tasksListDisplaysUpcomingTasks() {
        val repository = StubRepository
        val task1 = Task("TITLE_1", LocalDate.now())
        val task2 = Task("TITLE_2", LocalDate.of(5000, 2, 12))
        val task3 = Task("TITLE_3", LocalDate.of(2000, 11, 27))
        runBlocking { repository.addTasks(task1, task2, task3) }

        launchFragmentInContainer<UpcomingTasksFragment>(Bundle(), R.style.Theme_RemindMe)

        onView(withId(R.id.tasksList))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tasksList))
            .check(matches(hasDescendant(withText(task2.title))))
        onView(withId(R.id.tasksList))
            .check(matches(not(hasDescendant(withText(task1.title)))))
        onView(withId(R.id.tasksList))
            .check(matches(not(hasDescendant(withText(task3.title)))))
    }
}