package com.example.hundredplaces.data.model.image.datasources

import com.example.hundredplaces.data.model.image.ImagesRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


class ImagesRemoteDataSource(
    private val imagesRestApi: ImagesRestApi,
    private val networkConnection: NetworkConnection,
    private val refreshIntervalMs: Long = 60000
) {
    val allImages = flow {
        while (true) {
            if (networkConnection.isNetworkConnected) {
                val allImages = imagesRestApi.getAllImages()
                emit(allImages)
                delay(refreshIntervalMs)
            }
        }
    }
}