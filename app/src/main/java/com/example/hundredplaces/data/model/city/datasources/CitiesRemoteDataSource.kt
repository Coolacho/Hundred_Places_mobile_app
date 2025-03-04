package com.example.hundredplaces.data.model.city.datasources

import com.example.hundredplaces.data.model.city.CitiesRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class CitiesRemoteDataSource(
    private val citiesRestApi: CitiesRestApi,
    private val networkConnection: NetworkConnection,
    private val refreshIntervalMs: Long = 60000
) {
    val allCities = flow {
        while (true) {
            if (networkConnection.isNetworkConnected) {
                val allCities = citiesRestApi.getAllCities()
                emit(allCities)
                delay(refreshIntervalMs)
            }
        }
    }
}