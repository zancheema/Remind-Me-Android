package com.sleekdeveloper.android.remindme.data.source.remote

import retrofit2.http.*

interface RestApiService {
    @GET("/tasks/")
    suspend fun getTasks(): List<TaskDTO>

    @GET("/tasks/{taskId}")
    suspend fun getTaskById(@Path("taskId") taskId: String): TaskDTO

    @POST("/tasks")
    suspend fun saveTask(@Body task: TaskDTO)

    @PATCH("/tasks/{taskId}")
    suspend fun completeTask(@Path("taskId") taskId: String)

    @PATCH("/tasks")
    suspend fun updateTask(@Body task: TaskDTO)

    @DELETE("/tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String)

    @DELETE("/tasks")
    suspend fun deleteTasks()

    @DELETE("tasks/completed/true")
    suspend fun clearCompletedTasks()
}