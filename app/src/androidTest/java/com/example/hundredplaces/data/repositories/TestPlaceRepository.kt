package com.example.hundredplaces.data.repositories

import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestPlaceRepository : PlaceRepository {

    private val placesFlow: MutableSharedFlow<List<Place>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val placesWithCityAndImagesFlow: MutableSharedFlow<List<PlaceWithCityAndImages>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val placeWithCityAndImagesFlow: MutableSharedFlow<PlaceWithCityAndImages> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getAllPlaces() = placesFlow

    override fun getAllPlacesWithCityAndImages() = placesWithCityAndImagesFlow

    override fun findPlaceByCoordinates(
        latitude: Double,
        longitude: Double
    ): Long? {
        TODO("Not yet implemented")
    }

    override fun getPlaceWithCityAndImages(placeId: Long): Flow<PlaceWithCityAndImages?> = placeWithCityAndImagesFlow

    override suspend fun pullPlaces() {
        TODO("Not yet implemented")
    }

    fun sendPlaces(places: List<Place>) {
        placesFlow.tryEmit(places)
    }

    fun sendPlacesWithCityAndImages(places: List<PlaceWithCityAndImages>) {
        placesWithCityAndImagesFlow.tryEmit(places)
    }

    fun sendPlaceWithCityAndImages(place: PlaceWithCityAndImages) {
        placeWithCityAndImagesFlow.tryEmit(place)
    }
}