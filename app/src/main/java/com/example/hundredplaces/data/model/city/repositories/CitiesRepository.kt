package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [City] from a given data source.
 */
interface CitiesRepository {
    /**
     * Retrieve all the cities from the given data source.
     */
    suspend fun getAllCitiesStream(): Flow<List<City>>

    /**
     * Retrieve an city from the given data source that matches with the [id].
     */
    suspend fun getCityStream(id: Long): Flow<City?>

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