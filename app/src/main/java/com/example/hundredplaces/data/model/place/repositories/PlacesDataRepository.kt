package com.example.hundredplaces.data.model.place.repositories

import android.util.Log
import com.example.hundredplaces.data.model.place.datasources.PlacesLocalDataSource
import com.example.hundredplaces.data.model.place.datasources.PlacesRemoteDataSource
import kotlinx.coroutines.flow.catch

class PlacesDataRepository (
    private val placesLocalDataSource: PlacesLocalDataSource,
    private val placesRemoteDataSource: PlacesRemoteDataSource
) : PlacesRepository {

    override val allPlaces = placesLocalDataSource.allPlaces
    override val allPlacesWithCityAndImages = placesLocalDataSource.allPlacesWithCityAndImages

    override fun getPlaceWithCityAndImages(placeId: Long) = placesLocalDataSource.getPlaceWithCityAndImages(placeId)

    override suspend fun pullPlaces() {
        placesRemoteDataSource.allPlaces
            .catch { Log.e("Place flows", "${it.message}") }
            .collect { remotePlaces ->
                val remoteIds = remotePlaces.map { it.id }
                placesLocalDataSource.deletePlacesNotIn(remoteIds)
                placesLocalDataSource.insertAll(remotePlaces)
                Log.d("Flows test", "$remotePlaces")
            }
    }
}