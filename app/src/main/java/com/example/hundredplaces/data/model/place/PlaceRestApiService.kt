package com.example.hundredplaces.data.model.place

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val baseUrl = "/api/v1/places"
interface PlaceRestApiService {
    @GET("$baseUrl/all/filled")
    suspend fun getAllPlacesWithCityAndImages(): Flow<List<PlaceWithCityAndImages>>

    @GET("$baseUrl/all/plain")
    suspend fun getAllPlaces(): Flow<List<Place>>

    @GET("$baseUrl/place/{placeId}")
    suspend fun getPlace(@Path("placeId") id: Long): Flow<Place>

    @POST("$baseUrl/new")
    suspend fun insertPlace(@Body place: Place)

    @PUT("$baseUrl/update")
    suspend fun updatePlace(@Body place: Place)

    @DELETE("$baseUrl/delete")
    suspend fun deletePlace(@Body place: Place)
}