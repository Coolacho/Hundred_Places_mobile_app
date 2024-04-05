package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.CityDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [City] from a local data source.
 */
class LocalCitiesRepository(private val cityDao: CityDao) : CitiesRepository {
    /**
     * Retrieve all the cities from the local data source.
     */
    override suspend fun getAllCitiesStream(): Flow<List<City>> = cityDao.getAllCities()

    /**
     * Retrieve an city from the local data source that matches with the [id].
     */
    override suspend fun getCityStream(id: Int): Flow<City?> = cityDao.getCity(id)

    /**
     * Insert city in the local data source
     */
    override suspend fun insertCity(city: City) = cityDao.insert(city)

    /**
     * Delete city from the local data source
     */
    override suspend fun deleteCity(city: City) = cityDao.delete(city)

    /**
     * Update city in the local data source
     */
    override suspend fun updateCity(city: City) = cityDao.update(city)
}