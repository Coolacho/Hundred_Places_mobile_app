package com.example.hundredplaces.data.model.image.repositories

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow

class ImagesDataRepository(
    private val imagesLocalRepository: ImagesLocalRepository,
    private val imagesRemoteRepository: ImagesRemoteRepository,
    private val networkConnection: NetworkConnection
) : ImagesRepository{
    override suspend fun getAllImagesStream(): Flow<List<Image>> {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.getAllImagesStream()
        } else {
            imagesLocalRepository.getAllImagesStream()
        }
    }

    override suspend fun getImageStream(id: Int): Flow<Image?> {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.getImageStream(id)
        } else {
            imagesLocalRepository.getImageStream(id)
        }
    }

    override suspend fun insertImage(image: Image) {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.insertImage(image)
        } else {
            imagesLocalRepository.insertImage(image)
        }
    }

    override suspend fun deleteImage(image: Image) {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.deleteImage(image)
        } else {
            imagesLocalRepository.deleteImage(image)
        }
    }

    override suspend fun updateImage(image: Image) {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.updateImage(image)
        } else {
            imagesLocalRepository.updateImage(image)
        }
    }
}