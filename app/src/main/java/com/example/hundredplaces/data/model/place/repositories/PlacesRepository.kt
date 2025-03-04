package com.example.hundredplaces.data.model.place.repositories


import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides a [Flow] to retrieve all records of [Place] from a given data source.
 */
interface PlacesRepository {

    /**
     * Retrieve all the places from the given data source.
     */
    val allPlaces: Flow<List<Place>>

    /**
     * Retrieve all the places with city and images from the given data source.
     */
    val allPlacesWithCityAndImages: Flow<List<PlaceWithCityAndImages>>

    /**
     * Retrieve a place with city and images from the given data source.
     */
    fun getPlaceWithCityAndImages(placeId: Long): Flow<PlaceWithCityAndImages?>

    /**
     * Function to retrieve all places from the remote data source and save them in the local one
     */
    suspend fun pullPlaces()
}