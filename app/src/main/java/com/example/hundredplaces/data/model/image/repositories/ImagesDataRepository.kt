package com.example.hundredplaces.data.model.image.repositories

import android.util.Log
import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.datasources.ImagesLocalDataSource
import com.example.hundredplaces.data.model.image.datasources.ImagesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Implementation of [ImagesRepository] that has local and remote datasources. It collects a remote [Flow]
 * of all the records of [Image] and updates the local database. The repository emits a local flow
 * of all the records of [Image] for the UI to consume.
 */
class ImagesDataRepository(
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imagesRemoteDataSource: ImagesRemoteDataSource
) : ImagesRepository{

    override val allImages = imagesLocalDataSource.allImages

    override suspend fun pullImages() {
        imagesRemoteDataSource.allImages
            .catch { Log.e("Image flows", "${it.message}") }
            .collect { remoteImages ->
                val remoteIds = remoteImages.map { it.id }
                imagesLocalDataSource.deleteImagesNotIn(remoteIds)
                imagesLocalDataSource.insertAll(remoteImages)
                Log.d("Flows test", "$remoteImages")
            }
    }
}