package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City

/**
 * Repository that provides insert, update, delete, and retrieve of [City] from a given data source.
 */
interface CitiesRepository {
    /**
     * Retrieve all the cities from the given data source.
     */
    suspend fun getAllCities(): List<City>

    /**
     * Retrieve an city from the given data source that matches with the [id].
     */
    suspend fun getCity(id: Long): City

    /**
     * Insert city in the data source
     */
    suspend fun insertCity(city: City)

    /**
     * Delete city from the data source
     */
    suspend fun deleteCity(city: City)

    /**
     * Update city in the data source
     */
    suspend fun updateCity(city: City)
}