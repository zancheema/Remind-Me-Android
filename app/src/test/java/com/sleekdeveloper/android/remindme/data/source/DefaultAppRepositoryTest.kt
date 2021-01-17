package com.sleekdeveloper.android.remindme.data.source

import com.google.common.truth.Truth.assertThat
import com.sleekdeveloper.android.remindme.MainCoroutineRule
import com.sleekdeveloper.android.remindme.data.Result.Error
import com.sleekdeveloper.android.remindme.data.Result.Success
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class DefaultAppRepositoryTest {
    private val task1 = Task("title_1", LocalDate.now())
    private val task2 = Task("title_2", LocalDate.now())
    private val task3 = Task("title_3", LocalDate.now())
    private val newTask = Task("new_title", LocalDate.of(2050, 2, 1))
    private val remoteTasks = listOf(task1, task2)
    private val localTasks = listOf(task3)
    private val newTasks = listOf(newTask)
    private lateinit var remoteDataSource: FakeAppDataSource
    private lateinit var localDataSource: FakeAppDataSource

    // Class under test
    private lateinit var repository: DefaultAppRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        remoteDataSource = FakeAppDataSource(remoteTasks.toMutableList())
        localDataSource = FakeAppDataSource(localTasks.toMutableList())

        repository = DefaultAppRepository(
            remoteDataSource, localDataSource, Dispatchers.Main
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_emptyRepositoryAndUninitializedCache() = mainCoroutineRule.runBlockingTest {
        val emptySource = FakeAppDataSource()
        repository = DefaultAppRepository(
            emptySource, emptySource, Dispatchers.Main
        )

        assertThat(repository.getTasks() is Success).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_requestsAllTasksFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        val tasks = (repository.getTasks(true) as Success).data

        assertThat(tasks).isEqualTo(remoteDataSource.tasks)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_repositoryCachesAfterFirstApiCall() = mainCoroutineRule.runBlockingTest {
        val initial = (repository.getTasks() as Success).data
        assertThat(initial).isEqualTo(localDataSource.tasks)

        val second = (repository.getTasks(true) as Success).data
        assertThat(second).isEqualTo(remoteDataSource.tasks)
        assertThat(localDataSource.tasks).isEqualTo(remoteDataSource.tasks)
        assertThat(second).isNotEqualTo(initial)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveTask_savesToLocalAndRemote() = mainCoroutineRule.runBlockingTest {
        assertThat(remoteDataSource.tasks).doesNotContain(newTask)
        assertThat(localDataSource.tasks).doesNotContain(newTask)

        repository.saveTask(newTask)

        assertThat(remoteDataSource.tasks).contains(newTask)
        assertThat(localDataSource.tasks).contains(newTask)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_withDirtyCache_tasksAreRetrievedFromRemote() = mainCoroutineRule.runBlockingTest {
        val tasks = (repository.getTasks(true) as Success).data

        remoteDataSource.tasks = newTasks.toMutableList()

        val cachedTasks = (repository.getTasks() as Success).data
        assertThat(cachedTasks).isEqualTo(tasks)

        val refreshedTasks = (repository.getTasks(true) as Success).data
        assertThat(refreshedTasks).isEqualTo(newTasks)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_withDirtyCache_remoteUnavailable_error() = mainCoroutineRule.runBlockingTest {
        // When remote data source is unavailable
        remoteDataSource.tasks = null

        // Load tasks forcing remote load
        val refreshedTasks = repository.getTasks(true)

        // Result should be an error
        assertThat(refreshedTasks).isInstanceOf(Error::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_WithRemoteDataSourceUnavailable_tasksAreRetrievedFromLocal() =
        mainCoroutineRule.runBlockingTest {
            // When remote data source is unavailable
            remoteDataSource.tasks = null

            // Repository fetches from local source
            assertThat((repository.getTasks() as Success).data).isEqualTo(localTasks)
        }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_WithBothDataSourcesUnavailable_returnsError() = mainCoroutineRule.runBlockingTest {
        // When both sources are unavailable
        remoteDataSource.tasks = null
        localDataSource.tasks = null

        // The repository returns Error
        assertThat(repository.getTasks()).isInstanceOf(Error::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_refreshesLocalDataSource() = mainCoroutineRule.runBlockingTest {
        val initialLocal = localDataSource.tasks

        // First load will fetch from remote
        val newTasks = (repository.getTasks(true) as Success).data

        assertThat(newTasks).isEqualTo(remoteTasks)
        assertThat(newTasks).isEqualTo(localDataSource.tasks)
        assertThat(localDataSource.tasks).isEqualTo(initialLocal)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun completeTask_completesTaskToServiceAPIUpdatesCache() = mainCoroutineRule.runBlockingTest {
        repository.saveTask(newTask)

        assertThat((repository.getTaskWithId(newTask.id) as Success).data.isCompleted).isFalse()

        repository.completeTask(newTask.id)

        assertThat((repository.getTaskWithId(newTask.id) as Success).data.isCompleted).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clearCompletedTasks() = mainCoroutineRule.runBlockingTest {
        val completedTask = task1.copy(isCompleted = true)
        remoteDataSource.tasks = mutableListOf(completedTask, task2)
        repository.clearCompletedTasks()

        val tasks = (repository.getTasks(true) as Success).data
        assertThat(tasks).contains(task2)
        assertThat(tasks).doesNotContain(completedTask)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteAllTasks() = mainCoroutineRule.runBlockingTest {
        val initialTasks = (repository.getTasks() as Success).data

        repository.deleteAllTasks()

        val afterDeleteTasks = (repository.getTasks() as Success).data

        assertThat(initialTasks).isNotEmpty()
        assertThat(afterDeleteTasks).isEmpty()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteSingleTask() = mainCoroutineRule.runBlockingTest {
        val initialTasks = (repository.getTasks(true) as Success).data

        repository.deleteTask(task1.id)

        val afterDeletedTasks = (repository.getTasks() as Success).data

        assertThat(afterDeletedTasks.size).isEqualTo(initialTasks.size - 1)
        assertThat(afterDeletedTasks).doesNotContain(task1)
    }
}