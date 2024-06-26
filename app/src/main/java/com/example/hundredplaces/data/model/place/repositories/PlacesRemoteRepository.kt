package com.example.hundredplaces.data.model.place.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceRestApiService
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages

class PlacesRemoteRepository(
    private val placeRestApiService: PlaceRestApiService
) : PlacesRepository {
    override suspend fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages> = placeRestApiService.getAllPlacesWithCityAndImages()

    override suspend fun getAllPlaces(): List<Place> = placeRestApiService.getAllPlaces()
    override suspend fun getPlaceWithCityAndImages(id: Long): PlaceWithCityAndImages = placeRestApiService.getPlaceWithCityAndImages(id)

    override suspend fun getPlace(id: Long): Place = placeRestApiService.getPlace(id)

    override suspend fun insertPlace(place: Place) = placeRestApiService.insertPlace(place)

    override suspend fun deletePlace(place: Place) = placeRestApiService.deletePlace(place)

    override suspend fun updatePlace(place: Place) = placeRestApiService.updatePlace(place)

}