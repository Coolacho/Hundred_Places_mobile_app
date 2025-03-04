package com.example.hundredplaces.data.model.city

import retrofit2.http.GET

interface CitiesRestApi {

    @GET("/api/v1/cities")
    suspend fun getAllCities(): List<City>

}