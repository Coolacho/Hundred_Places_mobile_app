package com.example.hundredplaces.data.model.city.repositories

import com.example.hundredplaces.data.model.city.City
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides a [Flow] to retrieve all records of [City] from a given data source.
 */
interface CityRepository {

    /**
     * Function to retrieve cities from remote data source and save them to the local one
     */
    suspend fun pullCities()
}