package com.example.hundredplaces.data.model.city.repositories

import android.util.Log
import com.example.hundredplaces.data.model.city.datasources.CityRestApi
import com.example.hundredplaces.data.model.city.City
import com.example.hundredplaces.data.model.city.datasources.CityDao
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

/**
 * Implementation of [CityRepository] that has local and remote datasources. It fetches all the records
 * of [City] from remote data source and updates the local database. The repository provides a flow
 * of all the records of [City] in the local data source for the UI to consume.
 */
class DefaultCityRepository(
    private val cityLocalDataSource: CityDao,
    private val cityRemoteDataSource: CityRestApi,
    private val networkConnection: NetworkConnection,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CityRepository {

    override val allCities = cityLocalDataSource.getAll()

    override suspend fun pullCities() {
        withContext(dispatcher) {
            if (networkConnection.isNetworkConnected) {
                try {
                    val remoteCities = cityRemoteDataSource.getAllCities()
                    if (remoteCities.isNotEmpty()) {
                        val remoteIds = remoteCities.map { it.id }
                        cityLocalDataSource.deleteCitiesNotIn(remoteIds)
                        cityLocalDataSource.insertAll(remoteCities)
                        Log.d("City Repository", "$remoteCities")
                    }
                }
                catch (_: SocketTimeoutException) {
                    Log.e("City Repository", "Server is not reachable.")
                }
            }
        }
    }

}