package com.example.hundredplaces.data.model.place.repositories

import android.location.Location
import android.util.Log
import com.example.hundredplaces.data.model.place.datasources.PlaceDao
import com.example.hundredplaces.data.model.place.datasources.PlaceRestApi
import com.example.hundredplaces.util.NetworkMonitor

class DefaultPlaceRepository (
    private val placesLocalDataSource: PlaceDao,
    private val placesRemoteDataSource: PlaceRestApi,
    private val networkMonitor: NetworkMonitor
) : PlaceRepository {

    override fun getAllPlaces() = placesLocalDataSource.getAllPlaces()

    override fun getAllPlacesWithCityAndImages() = placesLocalDataSource.getAllPlacesWithCityAndImages()

    override fun findPlaceByCoordinates(latitude: Double, longitude: Double): Long? {
        val location = Location("").apply {
            this.latitude = latitude
            this.longitude = longitude
        }
        val placesCoordinates = placesLocalDataSource.getPlacesCoordinates()
        val filteredPlacesCoordinates =  placesCoordinates.filter { placeCoordinates ->
            val placeLocation = Location("").apply {
                this.latitude = placeCoordinates.latitude
                this.longitude = placeCoordinates.longitude
            }
            location.distanceTo(placeLocation) < 100f
        }
        if (filteredPlacesCoordinates.isNotEmpty()) {
            val sortedPlacesCoordinates = filteredPlacesCoordinates.sortedBy { placeCoordinates ->
                val placeLocation = Location("").apply {
                    this.latitude = placeCoordinates.latitude
                    this.longitude = placeCoordinates.longitude
                }
                location.distanceTo(placeLocation) < 100f
            }
            return sortedPlacesCoordinates[0].id
        }
        return null
    }

    override fun getPlaceWithCityAndImages(placeId: Long) = placesLocalDataSource.getPlaceWithCityAndImages(placeId)

    override suspend fun pullPlaces() {
        if (networkMonitor.isNetworkConnected) {
            val remotePlaces = placesRemoteDataSource.getAllPlaces()
            if (remotePlaces.isNotEmpty()) {
                val remoteIds = remotePlaces.map { it.id }
                placesLocalDataSource.deletePlacesNotIn(remoteIds)
                placesLocalDataSource.insertAll(remotePlaces)
                Log.d("Place Repository", "$remotePlaces")
            }
        }
    }
}