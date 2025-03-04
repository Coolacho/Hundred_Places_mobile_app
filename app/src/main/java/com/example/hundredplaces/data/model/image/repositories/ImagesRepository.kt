package com.example.hundredplaces.data.model.image.repositories


import com.example.hundredplaces.data.model.image.Image
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides a [Flow] to retrieve all records of [Image] from a given data source.
 */
interface ImagesRepository {
    /**
     * [Flow] to retrieve all images from the given data source.
     */
    val allImages: Flow<List<Image>>

    /**
     * Function to retrieve images from the remote data source and save them to the local one
     */
    suspend fun pullImages()
}