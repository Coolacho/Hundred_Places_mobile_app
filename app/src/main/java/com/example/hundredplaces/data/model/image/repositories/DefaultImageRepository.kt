package com.example.hundredplaces.data.model.image.repositories

import android.util.Log
import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.datasources.ImageDao
import com.example.hundredplaces.data.model.image.datasources.ImageRestApi

/**
 * Implementation of [ImageRepository] that has local and remote datasources. It fetches all the records
 * of [Image] from a remote data source and updates the local database. The repository provides a flow
 * of all the records of [Image] in the local data source for the UI to consume.
 */
class DefaultImageRepository(
    private val imageLocalDataSource: ImageDao,
    private val imageRemoteDataSource: ImageRestApi,
) : ImageRepository{

    override val allImages = imageLocalDataSource.getAll()

    override val oneImagePerPlace = imageLocalDataSource.getOneImagePerPlace()

    override suspend fun pullImages() {
        val remoteImages = imageRemoteDataSource.getAllImages()
        if (remoteImages.isNotEmpty()) {
            val remoteIds = remoteImages.map { it.id }
            imageLocalDataSource.deleteImagesNotIn(remoteIds)
            imageLocalDataSource.insertAll(remoteImages)
            Log.d("Image repository", "$remoteImages")
        }
    }
}