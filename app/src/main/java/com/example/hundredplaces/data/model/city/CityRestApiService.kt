package com.example.hundredplaces.data.model.city

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CityRestApiService {
    @GET("cities")
    suspend fun getAllCities(): Flow<List<City>>

    @GET("cities/{cityId}")
    suspend fun getCity(@Path("cityId") id: Int): Flow<City>

    @POST("cities/new")
    suspend fun insertCity(@Body city: City)

    @PUT("city/edit")
    suspend fun updateCity(@Body city: City)

    @DELETE("city/delete")
    suspend fun deleteCity(@Body city: City)
}