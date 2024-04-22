package com.example.hundredplaces.data.model.image.repositories

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.util.NetworkConnection

class ImagesDataRepository(
    private val imagesLocalRepository: ImagesLocalRepository,
    private val imagesRemoteRepository: ImagesRemoteRepository,
    private val networkConnection: NetworkConnection
) : ImagesRepository{
    override suspend fun getAllImages(): List<Image> {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.getAllImages()
        } else {
            imagesLocalRepository.getAllImages()
        }
    }

    override suspend fun getImage(id: Long): Image {
        return if (networkConnection.isNetworkConnected) {
            imagesRemoteRepository.getImage(id)
        } else {
            imagesLocalRepository.getImage(id)
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