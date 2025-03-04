package com.example.hundredplaces.data.model.place

import retrofit2.http.GET

interface PlacesRestApi {

    @GET("/api/v1/places")
    suspend fun getAllPlaces(): List<Place>

}