package com.sleekdeveloper.remindme.todaytasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sleekdeveloper.remindme.MainCoroutineRule
import com.sleekdeveloper.remindme.data.source.FakeTestRepository
import com.sleekdeveloper.remindme.data.source.domain.Task
import com.sleekdeveloper.remindme.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TodayTasksViewModelTest {
    private lateinit var repository: FakeTestRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initRepository() = runBlocking {
        repository = FakeTestRepository()
    }

    @Test
    fun tasks_LoadTodayTasks() = runBlocking {
        val task1 = Task("task_1", LocalDate.of(1999, 2, 2), LocalTime.now())
        val task2 = Task("task_2", LocalDate.now(), LocalTime.now())
        val task3 = Task("task_3", LocalDate.of(2011, 2, 3), LocalTime.now())
        repository.saveTasks(task1, task2, task3)
        val viewModel = TodayTasksViewModel(repository)

        val tasks = viewModel.tasks.getOrAwaitValue()

        assertThat(tasks, `is`(listOf(task2)))
    }

    @Test
    fun noTasks_GeneratesNoTaskEvent() {
        val viewModel = TodayTasksViewModel(repository)

        val tasks = viewModel.tasks.getOrAwaitValue()
        assertThat(tasks.isEmpty(), `is`(true))
        val event = viewModel.noTasksEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }

    @Test
    fun errorLoadingTasks_GeneratesTasksLoadingErrorEvent() {
        repository.setError(true)
        val viewModel = TodayTasksViewModel(repository)

        /* also activates the observer to set event */
        val tasks = viewModel.tasks.getOrAwaitValue()
        assertThat(tasks.isEmpty(), `is`(true))
        val event = viewModel.tasksLoadingErrorEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }

    @Test
    fun successLoadingTasks_SetsTasksLoadingErrorEventToFalse() {
        val viewModel = TodayTasksViewModel(repository)

        /* also activates the observer to set event */
        val tasks = viewModel.tasks.getOrAwaitValue()
        /* tasks loading error is not generated by empty tasks */
        assertThat(tasks.isEmpty(), `is`(true))
        val event = viewModel.tasksLoadingErrorEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(false))
    }
}