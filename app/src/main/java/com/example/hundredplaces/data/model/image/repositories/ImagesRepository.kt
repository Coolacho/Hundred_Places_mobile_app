package com.example.hundredplaces.data.model.image.repositories


import com.example.hundredplaces.data.model.image.Image

/**
 * Repository that provides insert, update, delete, and retrieve of [Image] from a given data source.
 */
interface ImagesRepository {
    /**
     * Retrieve all the images from the given data source.
     */
    suspend fun getAllImages(): List<Image>

    /**
     * Retrieve an image from the given data source that matches with the [id].
     */
    suspend fun getImage(id: Long): Image

    /**
     * Insert image in the data source
     */
    suspend fun insertImage(image: Image)

    /**
     * Delete image from the data source
     */
    suspend fun deleteImage(image: Image)

    /**
     * Update image in the data source
     */
    suspend fun updateImage(image: Image)
}