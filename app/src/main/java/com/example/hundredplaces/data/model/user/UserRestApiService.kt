package com.example.hundredplaces.data.model.user

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val baseUrl = "/api/v1/users"
interface UserRestApiService {
    @GET("$baseUrl/all")
    suspend fun getAllUsers(): Flow<List<User>>

    @GET("$baseUrl/user/plain/{userId}")
    suspend fun getUser(@Path("userId") id: Long): Flow<User>

    @GET("$baseUrl/user/filled/{userId}")
    suspend fun getUserWithVisits(@Path("userId") id: Long): Flow<UserWithVisits>

    @POST("users/new")
    suspend fun insertUser(@Body user: User)

    @PUT("user/edit")
    suspend fun updateUser(@Body user: User)

    @DELETE("user/delete")
    suspend fun deleteUser(@Body user: User)
}