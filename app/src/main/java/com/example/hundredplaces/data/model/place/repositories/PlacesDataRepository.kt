package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow

class PlacesDataRepository (
    private val placesLocalRepository: PlacesLocalRepository,
    private val placesRemoteRepository: PlacesRemoteRepository,
    private val networkConnection: NetworkConnection
) : PlacesRepository {
    override suspend fun getAllPlacesStream(): Flow<List<PlaceWithCityAndImages>> {
        return if (networkConnection.isNetworkConnected) {
           placesRemoteRepository. getAllPlacesStream()
        } else {
            placesLocalRepository.getAllPlacesStream()
        }
    }

    override suspend fun getPlaceStream(id: Int): Flow<PlaceWithCityAndImages?> {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository.getPlaceStream(id)
        } else {
            placesLocalRepository.getPlaceStream(id)
        }
    }

    override suspend fun insertPlace(place: Place) {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository.insertPlace(place)
        } else {
            placesLocalRepository.insertPlace(place)
        }
    }

    override suspend fun deletePlace(place: Place) {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository.deletePlace(place)
        } else {
            placesLocalRepository.deletePlace(place)
        }
    }

    override suspend fun updatePlace(place: Place) {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository.updatePlace(place)
        } else {
            placesLocalRepository.updatePlace(place)
        }
    }
}