package com.example.hundredplaces.data.model.place

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

const val baseUrl = "places"
interface PlaceRestApiService {
    @GET("$baseUrl/all/filled")
    suspend fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages>

    @GET("$baseUrl/place/filled")
    suspend fun getPlaceWithCityAndImages(@Query("id") id: Long): PlaceWithCityAndImages

    @GET("$baseUrl/all/plain")
    suspend fun getAllPlaces(): List<Place>

    @GET("$baseUrl/place/plain")
    suspend fun getPlace(@Query("id") id: Long): Place

    @POST("$baseUrl/new")
    suspend fun insertPlace(@Body place: Place)

    @PUT("$baseUrl/update")
    suspend fun updatePlace(@Body place: Place)

    @DELETE("$baseUrl/delete")
    suspend fun deletePlace(@Body place: Place)
}