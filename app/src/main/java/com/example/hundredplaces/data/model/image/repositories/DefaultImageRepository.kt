package com.example.hundredplaces.data.model.image.repositories

import android.util.Log
import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.datasources.ImageDao
import com.example.hundredplaces.data.model.image.datasources.ImageRestApi
import com.example.hundredplaces.util.NetworkMonitor

/**
 * Implementation of [ImageRepository] that has local and remote datasources. It fetches all the records
 * of [Image] from a remote data source and updates the local database. The repository provides a flow
 * of all the records of [Image] in the local data source for the UI to consume.
 */
class DefaultImageRepository(
    private val imageLocalDataSource: ImageDao,
    private val imageRemoteDataSource: ImageRestApi,
    private val networkMonitor: NetworkMonitor
) : ImageRepository{

    override val oneImagePerPlace = imageLocalDataSource.getOneImagePerPlace()

    override suspend fun pullImages() {
        if (networkMonitor.isNetworkConnected) {
            val remoteImages = imageRemoteDataSource.getAllImages()
            if (remoteImages.isNotEmpty()) {
                val remoteIds = remoteImages.map { it.id }
                imageLocalDataSource.deleteImagesNotIn(remoteIds)
                imageLocalDataSource.insertAll(remoteImages)
                Log.d("Image repository", "$remoteImages")
            }
        }
    }
}