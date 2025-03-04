package com.example.hundredplaces.data.model.city.repositories

import android.util.Log
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.datasources.CitiesLocalDataSource
import com.example.hundredplaces.data.model.city.datasources.CitiesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Implementation of [CitiesRepository] that has local and remote datasources. It collects a remote [Flow]
 * of all the records of [City] and updates the local database. The repository emits a local flow
 * of all the records of [City] for the UI to consume.
 */
class CitiesDataRepository(
    private val citiesLocalDataSource: CitiesLocalDataSource,
    private val citiesRemoteDataSource: CitiesRemoteDataSource
) : CitiesRepository{

    override val allCities = citiesLocalDataSource.allCities

    override suspend fun pullCities() {
        citiesRemoteDataSource.allCities
            .catch { Log.e("City flows", "${it.message}") }
            .collect { remoteCities ->
                val remoteIds = remoteCities.map { it.id }
                citiesLocalDataSource.deleteCitiesNotIn(remoteIds)
                citiesLocalDataSource.insertAll(remoteCities)
                Log.d("Flows test", "$remoteCities")
            }
    }

}