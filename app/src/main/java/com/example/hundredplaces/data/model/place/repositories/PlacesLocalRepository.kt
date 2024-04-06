package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceDao
import kotlinx.coroutines.flow.Flow

class PlacesLocalRepository(private val placeDao: PlaceDao) : PlacesRepository {
    override suspend fun getAllPlacesStream(): Flow<List<Place>> = placeDao.getAllPlaces()

    override suspend fun getPlaceStream(id: Int): Flow<Place?> = placeDao.getPlace(id)

    override suspend fun insertPlace(place: Place) = placeDao.insert(place)

    override suspend fun deletePlace(place: Place) = placeDao.delete(place)

    override suspend fun updatePlace(place: Place) = placeDao.update(place)
}