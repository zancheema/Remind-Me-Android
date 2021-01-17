package com.sleekdeveloper.android.remindme.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sleekdeveloper.android.remindme.MainCoroutineRule
import com.sleekdeveloper.android.remindme.data.source.domain.Task
import com.sleekdeveloper.android.remindme.data.source.domain.asDatabaseEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var tasksDao: TasksDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initDao() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        tasksDao = database.tasksDao()
    }

    @After
    fun closeDb() = database.close()

    @ExperimentalCoroutinesApi
    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        val task = Task("title", LocalDate.now())
        tasksDao.insertTask(task.asDatabaseEntity())

        val loaded: DbTask? = tasksDao.getTaskById(task.id)
        assertThat(loaded as DbTask, `is`(notNullValue()))
        assertThat(loaded.asDomainModel(), `is`(task))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertTaskReplacesOnConflict() = runBlockingTest {
        val task = Task("title", LocalDate.now())
        tasksDao.insertTask(task.asDatabaseEntity())

        val updatedTask = task.copy(title = "updated title")
        tasksDao.insertTask(updatedTask.asDatabaseEntity())

        val loaded: DbTask? = tasksDao.getTaskById(task.id)
        assertThat(loaded as DbTask, `is`(notNullValue()))
        assertThat(loaded.asDomainModel(), `is`(updatedTask))
        assertThat(loaded.asDomainModel(), `is`(not(task)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertTaskAndGetTasks() = runBlockingTest {
        val task = Task("title", LocalDate.now())
        tasksDao.insertTask(task.asDatabaseEntity())

        val loadedTasks = tasksDao.getTasks()
        assertThat(loadedTasks.size, `is`(1))
        assertThat(loadedTasks[0].asDomainModel(), `is`(task))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateCompletedAndGetById() = runBlockingTest {
        val task = Task("title", LocalDate.now(), isCompleted = true)
        tasksDao.insertTask(task.asDatabaseEntity())

        tasksDao.updateCompleted(task.id, false)

        val updatedTask = task.copy(isCompleted = false)
        val loaded: DbTask? = tasksDao.getTaskById(task.id)
        assertThat(loaded as DbTask, `is`(notNullValue()))
        assertThat(loaded.asDomainModel(), `is`(updatedTask))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteTaskByIdAndGettingTasks() = runBlockingTest {
        val task = Task("title", LocalDate.now(), isCompleted = true)
        tasksDao.insertTask(task.asDatabaseEntity())

        tasksDao.deleteTaskById(task.id)

        val tasks = tasksDao.getTasks()
        assertThat(tasks.isEmpty(), `is`(true))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteTasksAndGettingTasks() = runBlockingTest {
        val task1 = Task("title 1", LocalDate.now())
        val task2 = Task("title 2", LocalDate.now())
        tasksDao.insertTask(task1.asDatabaseEntity())
        tasksDao.insertTask(task2.asDatabaseEntity())

        tasksDao.deleteTasks()

        val tasks = tasksDao.getTasks()
        assertThat(tasks.isEmpty(), `is`(true))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteCompletedTasksAndGettingTasks() = runBlockingTest {
        val completedTask = Task("title 1", LocalDate.now(), isCompleted = true)
        val incompleteTask = Task("title 2", LocalDate.now())
        tasksDao.insertTask(completedTask.asDatabaseEntity())
        tasksDao.insertTask(incompleteTask.asDatabaseEntity())

        tasksDao.deleteCompletedTasks()

        val tasks = tasksDao.getTasks()
        assertThat(tasks.size, `is`(1))
        assertThat(tasks[0].asDomainModel(), `is`(incompleteTask))
    }
}