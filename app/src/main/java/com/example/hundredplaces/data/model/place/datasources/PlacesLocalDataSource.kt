package com.example.hundredplaces.data.model.place.datasources

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceDao

class PlacesLocalDataSource(
    private val placeDao: PlaceDao
) {

    val allPlaces = placeDao.getAllPlaces()
    val allPlacesWithCityAndImages = placeDao.getAllPlacesWithCityAndImages()

    fun getPlaceWithCityAndImages(placeId: Long) = placeDao.getPlaceWithCityAndImages(placeId)

    suspend fun insertAll(places: List<Place>) = placeDao.insertAll(places)

    suspend fun deletePlacesNotIn(ids: List<Long>) = placeDao.deletePlacesNotIn(ids)
}