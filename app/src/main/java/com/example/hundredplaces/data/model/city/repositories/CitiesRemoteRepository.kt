package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityRestApiService
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [City] from a remote data source.
 */
class CitiesRemoteRepository(
    private val cityRestApiService: CityRestApiService
) : CitiesRepository {
    /**
     * Retrieve all the cities from the remote data source.
     */
    override suspend fun getAllCitiesStream(): Flow<List<City>> = cityRestApiService.getAllCities()

    /**
     * Retrieve an city from the remote data source that matches with the [id].
     */
    override suspend fun getCityStream(id: Int): Flow<City?> = cityRestApiService.getCity(id)

    /**
     * Insert city in the remote data source
     */
    override suspend fun insertCity(city: City) = cityRestApiService.insertCity(city)

    /**
     * Delete city from the remote data source
     */
    override suspend fun deleteCity(city: City) = cityRestApiService.deleteCity(city)

    /**
     * Update city in the remote data source
     */
    override suspend fun updateCity(city: City) = cityRestApiService.updateCity(city)
}