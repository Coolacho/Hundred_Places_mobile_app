package com.example.hundredplaces.data.model.place

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlaceRestApiService {
    @GET("places")
    suspend fun getAllPlaces(): Flow<List<Place>>

    @GET("places/{placeId}")
    suspend fun getPlace(@Path("placeId") id: Int): Flow<PlaceWithCityAndImages>

    @POST("places/new")
    suspend fun insertPlace(@Body place: Place)

    @PUT("place/edit")
    suspend fun updatePlace(@Body place: Place)

    @DELETE("place/delete")
    suspend fun deletePlace(@Body place: Place)
}