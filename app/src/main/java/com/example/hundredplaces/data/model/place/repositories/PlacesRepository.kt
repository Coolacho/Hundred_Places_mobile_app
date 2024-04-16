package com.example.hundredplaces.data.model.place.repositories


import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert, update, delete, and retrieve of [Place] from a given data source.
 */
interface PlacesRepository {
    /**
     * Retrieve all the places with city and images from the given data source.
     */
    suspend fun getAllPlacesWithCityAndImagesStream(): Flow<List<PlaceWithCityAndImages>>

    /**
     * Retrieve all the places from the given data source.
     */
    suspend fun getAllPlacesStream(): Flow<List<Place>>

    /**
     * Retrieve a place from the given data source
     */
    suspend fun getPlaceStream(id: Long): Flow<Place>

    /**
     * Insert place in the data source
     */
    suspend fun insertPlace(place: Place)

    /**
     * Delete place from the data source
     */
    suspend fun deletePlace(place: Place)

    /**
     * Update place in the data source
     */
    suspend fun updatePlace(place: Place)
}