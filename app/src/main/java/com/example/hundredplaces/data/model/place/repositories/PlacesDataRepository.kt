package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.util.NetworkConnection
import java.net.SocketTimeoutException

class PlacesDataRepository (
    private val placesLocalRepository: PlacesLocalRepository,
    private val placesRemoteRepository: PlacesRemoteRepository,
    private val networkConnection: NetworkConnection
) : PlacesRepository {
    override suspend fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages> {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository.getAllPlacesWithCityAndImages()
            } catch (e: SocketTimeoutException) {
                placesLocalRepository.getAllPlacesWithCityAndImages()
            }
        } else {
            placesLocalRepository.getAllPlacesWithCityAndImages()
        }
    }

    override suspend fun getAllPlaces(): List<Place> {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository. getAllPlaces()
            } catch (e: SocketTimeoutException) {
                placesLocalRepository.getAllPlaces()
            }
        } else {
            placesLocalRepository.getAllPlaces()
        }
    }

    override suspend fun getPlaceWithCityAndImages(id: Long): PlaceWithCityAndImages {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository.getPlaceWithCityAndImages(id)
            } catch (e: SocketTimeoutException) {
                placesLocalRepository.getPlaceWithCityAndImages(id)
            }
        } else {
            placesLocalRepository.getPlaceWithCityAndImages(id)
        }
    }

    override suspend fun getPlace(id: Long): Place {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository.getPlace(id)
            } catch (e: SocketTimeoutException) {
                placesLocalRepository.getPlace(id)
            }
        } else {
            placesLocalRepository.getPlace(id)
        }
    }

    override suspend fun insertPlace(place: Place) {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository.insertPlace(place)
            } catch (e: SocketTimeoutException) {
                placesLocalRepository.insertPlace(place)
            }
        } else {
            placesLocalRepository.insertPlace(place)
        }
    }

    override suspend fun deletePlace(place: Place) {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository.deletePlace(place)
            } catch (e: SocketTimeoutException) {
                placesLocalRepository.deletePlace(place)
            }
        } else {
            placesLocalRepository.deletePlace(place)
        }
    }

    override suspend fun updatePlace(place: Place) {
        return if (networkConnection.isNetworkConnected) {
            try {
                placesRemoteRepository.updatePlace(place)
            } catch (e: Exception) {
                placesLocalRepository.updatePlace(place)
            }
        } else {
            placesLocalRepository.updatePlace(place)
        }
    }
}