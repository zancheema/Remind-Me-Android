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
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalCoroutinesApi
class TasksViewModelTest {
    private lateinit var repository: FakeTestRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initRepository() = runBlocking {
        repository = FakeTestRepository
    }

    @Test
    fun allTasks_ReturnsAllInsertedTasksAndSetsTasksLoadingErrorEventToFalse() = runBlocking {
        val task1 = Task("task_1", LocalDate.of(1999, 2, 2), LocalTime.now())
        val task2 = Task("task_2", LocalDate.now(), LocalTime.now())
        val task3 = Task("task_3", LocalDate.of(2011, 2, 3), LocalTime.now())
        repository.addTasks(task1, task2, task3)
        val viewModel = TasksViewModel(repository)

        val tasks = viewModel.allTasks.getOrAwaitValue()
        tasks.sortedBy { it.id }

        assertThat(tasks, `is`(listOf(task1, task2, task3)))
        val errorEvent = viewModel.tasksLoadingErrorEvent.getOrAwaitValue()
        assertThat(errorEvent.getContentIfNotHandled(), `is`(false))
    }

    @Test
    fun delayedTasks_ReturnTasksOfPreviousDates() = runBlocking {
        val task1 = Task("task_1", LocalDate.of(1999, 2, 2), LocalTime.now())
        val task2 = Task("task_2", LocalDate.now(), LocalTime.now())
        val task3 = Task("task_3", LocalDate.of(2011, 2, 3), LocalTime.now())
        repository.addTasks(task1, task2, task3)
        val viewModel = TasksViewModel(repository)

        val tasks = viewModel.delayedTasks.getOrAwaitValue()
        tasks.sortedBy { it.id }

        assertThat(tasks, `is`(listOf(task1, task3)))
    }

    @Test
    fun todayTasks_ReturnTasksOfToday() = runBlocking {
        val task1 = Task("task_1", LocalDate.of(1999, 2, 2), LocalTime.now())
        val task2 = Task("task_2", LocalDate.now(), LocalTime.now())
        val task3 = Task("task_3", LocalDate.of(2011, 2, 3), LocalTime.now())
        repository.addTasks(task1, task2, task3)
        val viewModel = TasksViewModel(repository)

        val tasks = viewModel.todayTasks.getOrAwaitValue()

        assertThat(tasks, `is`(listOf(task2)))
    }

    @Test
    fun upcomingTasks_ReturnTasksOfUpcomingDates() = runBlocking {
        val currentDate = LocalDate.now()
        val task1 = Task("task_1", LocalDate.of(1999, 2, 2), LocalTime.now())
        val task2 = Task("task_2", LocalDate.now(), LocalTime.now())
        val task3 = Task(
            "task_3",
            LocalDate.of(currentDate.year + 1, currentDate.month, currentDate.dayOfMonth),
            LocalTime.now()
        )
        repository.addTasks(task1, task2, task3)
        val viewModel = TasksViewModel(repository)

        val tasks = viewModel.upcomingTasks.getOrAwaitValue()

        assertThat(tasks, `is`(listOf(task3)))
    }

    @Test
    fun allTasks_NoTasksInserted_GeneratesNoTasksEvent() {
        val viewModel = TasksViewModel(repository)

        val event = viewModel.noTasksEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }

    @Test
    fun errorLoadingTasks_GeneratesTasksLoadingErrorEvent() {
        repository.setError(true)
        val viewModel = TasksViewModel(repository)

        assertThat(viewModel.allTasks.getOrAwaitValue(), `is`(emptyList()))
        val errorEvent = viewModel.tasksLoadingErrorEvent.getOrAwaitValue()
        assertThat(errorEvent.getContentIfNotHandled(), `is`(true))
    }

    @Test
    fun addTask_GeneratesAddTaskEvent() {
        val viewModel = TasksViewModel(repository)
        viewModel.addTask()

        val event = viewModel.addTaskEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }
}