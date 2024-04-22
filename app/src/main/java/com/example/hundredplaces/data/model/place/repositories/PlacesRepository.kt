package com.example.hundredplaces.data.model.place.repositories


import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages


/**
 * Repository that provides insert, update, delete, and retrieve of [Place] from a given data source.
 */
interface PlacesRepository {
    /**
     * Retrieve all the places with city and images from the given data source.
     */
    suspend fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages>

    /**
     * Retrieve all the places from the given data source.
     */
    suspend fun getAllPlaces(): List<Place>

    /**
     * Retrieve a place with city and images from the given data source
     */
    suspend fun getPlaceWithCityAndImages(id: Long): PlaceWithCityAndImages

    /**
     * Retrieve a place from the given data source
     */
    suspend fun getPlace(id: Long): Place

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