package com.example.hundredplaces.data.model.user

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

const val baseUrl = "users"
interface UserRestApiService {

    @GET("$baseUrl/user/login")
    suspend fun getUserByEmailAndPassword(
        @Query("email") email: String,
        @Query("password") password: String
    ): User

    @GET("$baseUrl/user/existing")
    suspend fun getUserByEmail(
        @Query("email") email: String
    ): User

    @POST("$baseUrl/new")
    suspend fun insertUser(@Body user: User)

    @PUT("$baseUrl/update")
    suspend fun updateUser(@Body user: User)

    @DELETE("$baseUrl/delete")
    suspend fun deleteUser(@Body user: User)
}