package com.example.hundredplaces.data.model.city

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val baseUrl = "/api/v1/cities"
interface CityRestApiService {
    @GET("$baseUrl/all")
    suspend fun getAllCities(): Flow<List<City>>

    @GET("$baseUrl/city/{cityId}")
    suspend fun getCity(@Path("cityId") id: Long): Flow<City>

    @POST("$baseUrl/new")
    suspend fun insertCity(@Body city: City)

    @PUT("$baseUrl/update")
    suspend fun updateCity(@Body city: City)

    @DELETE("$baseUrl/delete")
    suspend fun deleteCity(@Body city: City)
}