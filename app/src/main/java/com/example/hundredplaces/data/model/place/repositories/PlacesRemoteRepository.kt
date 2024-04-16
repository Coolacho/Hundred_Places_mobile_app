package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceRestApiService
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlinx.coroutines.flow.Flow

class PlacesRemoteRepository(
    private val placeRestApiService: PlaceRestApiService
) : PlacesRepository {
    override suspend fun getAllPlacesWithCityAndImagesStream(): Flow<List<PlaceWithCityAndImages>> = placeRestApiService.getAllPlacesWithCityAndImages()

    override suspend fun getAllPlacesStream(): Flow<List<Place>> = placeRestApiService.getAllPlaces()

    override suspend fun getPlaceStream(id: Long): Flow<Place> = placeRestApiService.getPlace(id)

    override suspend fun insertPlace(place: Place) = placeRestApiService.insertPlace(place)

    override suspend fun deletePlace(place: Place) = placeRestApiService.deletePlace(place)

    override suspend fun updatePlace(place: Place) = placeRestApiService.updatePlace(place)

}