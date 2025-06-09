package com.example.hundredplaces.data.model.place.datasources

import com.example.hundredplaces.data.model.place.Place
import retrofit2.http.GET

interface PlaceRestApi {

    @GET("/api/v1/places")
    suspend fun getAllPlaces(): List<Place>

}