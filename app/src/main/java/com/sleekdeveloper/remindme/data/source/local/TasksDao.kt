package com.sleekdeveloper.remindme.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): DbTask?

    @Query("SELECT * FROM tasks")
    fun observeTasks(): LiveData<List<DbTask>>

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<DbTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(asDatabaseEntity: DbTask)

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)

    @Query("DELETE FROM tasks")
    suspend fun deleteTasks()

    @Query("DELETE FROM tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}
