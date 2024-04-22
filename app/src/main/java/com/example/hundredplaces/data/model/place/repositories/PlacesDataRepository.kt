package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.util.NetworkConnection

class PlacesDataRepository (
    private val placesLocalRepository: PlacesLocalRepository,
    private val placesRemoteRepository: PlacesRemoteRepository,
    private val networkConnection: NetworkConnection
) : PlacesRepository {
    override suspend fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages> {
        return if (networkConnection.isNetworkConnected) {
           placesRemoteRepository. getAllPlacesWithCityAndImages()
        } else {
            placesLocalRepository.getAllPlacesWithCityAndImages()
        }
    }

    override suspend fun getAllPlaces(): List<Place> {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository. getAllPlaces()
        } else {
            placesLocalRepository.getAllPlaces()
        }
    }

    override suspend fun getPlaceWithCityAndImages(id: Long): PlaceWithCityAndImages {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository.getPlaceWithCityAndImages(id)
        } else {
            placesLocalRepository.getPlaceWithCityAndImages(id)
        }
    }

    override suspend fun getPlace(id: Long): Place {
        return if (networkConnection.isNetworkConnected) {
            placesRemoteRepository.getPlace(id)
        } else {
            placesLocalRepository.getPlace(id)
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