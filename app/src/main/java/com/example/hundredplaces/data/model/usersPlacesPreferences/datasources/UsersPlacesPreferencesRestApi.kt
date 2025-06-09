@file:JvmName("UsersPlacesPreferencesKt")

package com.example.hundredplaces.data.model.usersPlacesPreferences.datasources

import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val baseUrl = "/api/v1/usersPlacesPreferences"
interface UsersPlacesPreferencesRestApi {
    @GET(baseUrl)
    suspend fun getUsersPlacesPreferencesByUserId(@Query("userId") userId: Long): List<UsersPlacesPreferences>

    @POST("$baseUrl/new")
    suspend fun insertAll(@Body usersPlacesPreferences: List<UsersPlacesPreferences>)

}