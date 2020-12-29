package com.sleekdeveloper.remindme.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sleekdeveloper.remindme.MainCoroutineRule
import com.sleekdeveloper.remindme.data.source.FakeTestRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.System.currentTimeMillis
import java.sql.Timestamp

@ExperimentalCoroutinesApi
class TasksViewModelTest {
    private lateinit var viewModel: TasksViewModel
    private lateinit var repository: FakeTestRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule =  MainCoroutineRule()

    @Before
    fun initRepository() = runBlocking {
        repository = FakeTestRepository()
        viewModel = TasksViewModel(repository)
    }

    @Test
    fun delayedTasks_ReturnTasksOfPreviousDates() = runBlocking {
        val task1 = Task("task_1", "TITLE", Timestamp(2000L))
        val task2 = Task("task_2", "TITLE", Timestamp(currentTimeMillis()))
        val task3 = Task("task_3", "TITLE", Timestamp(3000L))
        listOf(task1, task2, task3).forEach { repository.addTask(it) }

        val tasks = viewModel.delayedTasks.getOrAwaitValue()
        tasks.sortedBy { it.id }

        assertThat(tasks, `is`(listOf(task1, task3)))
    }

    @Test
    fun todayTasks_ReturnTasksOfToday() = runBlocking {
        val task1 = Task("task_1", "TITLE", Timestamp(2000L))
        val task2 = Task("task_2", "TITLE", Timestamp(currentTimeMillis()))
        val task3 = Task("task_3", "TITLE", Timestamp(3000L))
        listOf(task1, task2, task3).forEach { repository.addTask(it) }

        val tasks = viewModel.todayTasks.getOrAwaitValue()

        assertThat(tasks, `is`(listOf(task2)))
    }

    @Test
    fun upcomingTasks_ReturnTasksOfUpcomingDates() = runBlocking {
        val oneDayInMillis = 86400000L
        val task1 = Task("task_1", "TITLE", Timestamp(2000L))
        val task2 = Task("task_2", "TITLE", Timestamp(currentTimeMillis()))
        val task3 = Task("task_3", "TITLE", Timestamp(currentTimeMillis() + 2 * oneDayInMillis))
        listOf(task1, task2, task3).forEach { repository.addTask(it) }

        val tasks = viewModel.upcomingTasks.getOrAwaitValue()

        assertThat(tasks, `is`(listOf(task3)))
    }

    @Test
    fun addTask_GeneratesAddTaskEvent() {
        viewModel.addTask()

        val event = viewModel.addTaskEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }
}