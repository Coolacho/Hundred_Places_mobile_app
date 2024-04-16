package com.example.hundredplaces.data.model.user

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserRestApiService {
    @GET("users")
    suspend fun getAllUsers(): Flow<List<User>>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") id: Long): Flow<User>

    @POST("users/new")
    suspend fun insertUser(@Body user: User)

    @PUT("user/edit")
    suspend fun updateUser(@Body user: User)

    @DELETE("user/delete")
    suspend fun deleteUser(@Body user: User)
}