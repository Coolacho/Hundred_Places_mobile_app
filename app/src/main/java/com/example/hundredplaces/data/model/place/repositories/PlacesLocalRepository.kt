package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceDao
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlinx.coroutines.flow.Flow

class PlacesLocalRepository(private val placeDao: PlaceDao) : PlacesRepository {
    override suspend fun getAllPlacesStream(): Flow<List<PlaceWithCityAndImages>> = placeDao.getAllPlaces()

    override suspend fun getPlaceStream(id: Int): Flow<PlaceWithCityAndImages?> = placeDao.getPlace(id)

    override suspend fun insertPlace(place: Place) = placeDao.insert(place)

    override suspend fun deletePlace(place: Place) = placeDao.delete(place)

    override suspend fun updatePlace(place: Place) = placeDao.update(place)
}