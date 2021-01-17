package com.sleekdeveloper.remindme.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sleekdeveloper.remindme.MainCoroutineRule
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * Integration test for [LocalDataSource]
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest {

    private lateinit var database: AppDatabase
    private lateinit var localDataSource: LocalDataSource

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = LocalDataSource(database, Dispatchers.Main)
    }

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun saveTask_retrievesTask() = runBlockingTest {
        val task = Task("TITLE", LocalDate.now())
        localDataSource.saveTask(task)

        val result = localDataSource.getTaskWithId(task.id)

        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data, `is`(task))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlockingTest {
        val task = Task("TITLE", LocalDate.now())
        localDataSource.saveTask(task)

        localDataSource.completeTask(task)
        val completedTask = task.copy(isCompleted = true)
        val result = localDataSource.getTaskWithId(task.id)

        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data, `is`(completedTask))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clearCompletedTask_taskNotRetrievable() = runBlockingTest {
        val task1 = Task("TITLE 1", LocalDate.now())
        val task2 = Task("TITLE 2", LocalDate.now())
        val task3 = Task("TITLE 3", LocalDate.now())
        localDataSource.saveTask(task1)
        localDataSource.completeTask(task1)
        localDataSource.saveTask(task2)
        localDataSource.completeTask(task2)
        localDataSource.saveTask(task3)

        localDataSource.clearCompletedTasks()

        val result1 = localDataSource.getTaskWithId(task1.id)
        assertThat(result1.succeeded, `is`(false))
        val result2 = localDataSource.getTaskWithId(task2.id)
        assertThat(result2.succeeded, `is`(false))
        val result3 = localDataSource.getTaskWithId(task3.id)
        assertThat(result3.succeeded, `is`(true))
        result3 as Success
        assertThat(result3.data, `is`(task3))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteAllTasks_emptyListOfRetrievedTask() = runBlockingTest {
        val task = Task("TITLE", LocalDate.now())
        localDataSource.saveTask(task)

        localDataSource.deleteTask(task.id)

        val result = localDataSource.getTaskWithId(task.id)
        assertThat(result.succeeded, `is`(false))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_retrieveSavedTasks() = runBlockingTest {
        val task1 = Task("TITLE 1", LocalDate.now())
        val task2 = Task("TITLE 1", LocalDate.now())
        localDataSource.saveTask(task1)
        localDataSource.saveTask(task2)

        localDataSource.deleteAllTasks()

        val result = localDataSource.getTasks()
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.size, `is`(0))
    }
}