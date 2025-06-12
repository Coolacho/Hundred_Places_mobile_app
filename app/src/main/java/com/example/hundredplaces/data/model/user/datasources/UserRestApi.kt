package com.example.hundredplaces.data.model.user.datasources

import com.example.hundredplaces.data.model.user.UpdatePasswordRequest
import com.example.hundredplaces.data.model.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

const val baseUrl = "/api/v1/users"
interface UserRestApi {

    @GET("$baseUrl/login")
    suspend fun getUserByEmailAndPassword(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<User>

    @GET("$baseUrl/exists")
    suspend fun getIsUserExistingById(@Query("userId") userId: Long): Boolean

    @POST("$baseUrl/new")
    suspend fun insert(@Body user: User)

    @PUT("$baseUrl/updateDetails")
    suspend fun updateDetails(@Body user: User)

    @PUT("$baseUrl/updatePassword")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest)

    @DELETE("$baseUrl/delete")
    suspend fun delete(@Body user: User)
}