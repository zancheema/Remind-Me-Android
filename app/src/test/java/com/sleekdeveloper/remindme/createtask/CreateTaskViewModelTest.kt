package com.sleekdeveloper.remindme.createtask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sleekdeveloper.remindme.MainCoroutineRule
import com.sleekdeveloper.remindme.R
import com.sleekdeveloper.remindme.data.Result.Success
import com.sleekdeveloper.remindme.data.source.FakeTestRepository
import com.sleekdeveloper.remindme.getOrAwaitValue
import com.sleekdeveloper.remindme.util.TaskRepeatOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalCoroutinesApi
class CreateTaskViewModelTest {
    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var repository: FakeTestRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initRepository() {
        repository = FakeTestRepository()
        viewModel = CreateTaskViewModel(repository)
    }

    @Test
    fun createTaskWithoutTitle_SetsUpSnackbarTextToEmptyTitle() {
        viewModel.createTask()

        val event = viewModel.snackbarText.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(R.string.empty_title_event))
    }

    @Test
    fun createTaskWithoutDate_SetsUpSnackbarTextToEmptyDate() {
        viewModel.title.value = "TITLE"
        // no date selected
        viewModel.createTask()

        val event = viewModel.snackbarText.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(R.string.empty_date_event))
    }

    @Test
    fun taskRepeatOptions_ReturnsOnceAndDaily() {
        val options: List<String> = viewModel.taskRepeatOptionNames.getOrAwaitValue()

        assertThat(options[0], `is`("Once"))
        assertThat(options[1], `is`("Daily"))
    }

    @Test
    fun createTaskWithValidFields_CreatesTaskWithSelectedFields() {
        val title = "TITLE"
        val date = LocalDate.of(2000, 4, 8)
        val time = LocalTime.of(18, 55)

        viewModel.title.value = title
        viewModel.selectedOptionIndex.value = 1
        viewModel.setDate(date)
        viewModel.setTime(time)
        viewModel.createTask()

        val tasks = (repository.observeAllTasks().getOrAwaitValue() as Success).data
        assertThat(tasks.size, `is`(1))
        val createdTask = tasks[0]
        assertThat(createdTask.title, `is`(title))
        assertThat(createdTask.repeat, `is`(TaskRepeatOption.Daily))
        assertThat(createdTask.date, `is`(date))
        assertThat(createdTask.time, `is`(time))
    }
}