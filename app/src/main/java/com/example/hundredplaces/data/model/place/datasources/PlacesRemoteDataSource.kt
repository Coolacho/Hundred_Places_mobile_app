package com.example.hundredplaces.data.model.place.datasources

import com.example.hundredplaces.data.model.place.PlacesRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class PlacesRemoteDataSource(
    private val placesRestApi: PlacesRestApi,
    private val networkConnection: NetworkConnection,
    private val refreshIntervalMs: Long = 60000
) {
    val allPlaces = flow {
        while (true) {
            if (networkConnection.isNetworkConnected) {
                val allPlaces = placesRestApi.getAllPlaces()
                emit(allPlaces)
                delay(refreshIntervalMs)
            }
        }
    }

}