package com.example.hundredplaces.data.model.city.datasources

import com.example.hundredplaces.data.model.city.City
import retrofit2.http.GET

interface CityRestApi {

    @GET("/api/v1/cities")
    suspend fun getAllCities(): List<City>

}