package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceDao
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages

class PlacesLocalRepository(private val placeDao: PlaceDao) : PlacesRepository {
    override suspend fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages> = placeDao.getAllPlacesWithCityAndImages()

    override suspend fun getAllPlaces(): List<Place> = placeDao.getAllPlaces()
    override suspend fun getPlaceWithCityAndImages(id: Long): PlaceWithCityAndImages = placeDao.getPlaceWithCityAndImages(id)

    override suspend fun getPlace(id: Long): Place = placeDao.getPlace(id)

    override suspend fun insertPlace(place: Place) = placeDao.insert(place)

    override suspend fun deletePlace(place: Place) = placeDao.delete(place)

    override suspend fun updatePlace(place: Place) = placeDao.update(place)
}