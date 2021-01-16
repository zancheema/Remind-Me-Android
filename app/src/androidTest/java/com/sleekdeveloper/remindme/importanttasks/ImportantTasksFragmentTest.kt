package com.sleekdeveloper.remindme.importanttasks

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sleekdeveloper.remindme.R
import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.di.AppRepositoryModule
import com.sleekdeveloper.remindme.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@UninstallModules(AppRepositoryModule::class)
@HiltAndroidTest
class ImportantTasksFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun tasksListDisplaysImportantTasks() {
        val task1 = Task("TITLE_1", LocalDate.now(), isImportant = true)
        val task2 = Task("TITLE_2", LocalDate.now(), isImportant = true)
        val task3 = Task("TITLE_3", LocalDate.now())
        runBlocking { repository.saveTasks(task1, task2, task3) }

        launchFragmentInHiltContainer<ImportantTasksFragment>(Bundle(), R.style.Theme_RemindMe)

        onView(withId(R.id.tasksList))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tasksList))
            .check(matches(hasDescendant(withText(task1.title))))
        onView(withId(R.id.tasksList))
            .check(matches(hasDescendant(withText(task2.title))))
        onView(withId(R.id.tasksList))
            .check(matches(not(hasDescendant(withText(task3.title)))))
    }
}