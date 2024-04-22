package com.example.hundredplaces.data.model.image.repositories

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.ImageRestApiService

class ImagesRemoteRepository(
    private val imageRestApiService: ImageRestApiService
) : ImagesRepository{
    override suspend fun getAllImages(): List<Image> = imageRestApiService.getAllImages()

    override suspend fun getImage(id: Long): Image = imageRestApiService.getImage(id)

    override suspend fun insertImage(image: Image) = imageRestApiService.insertImage(image)

    override suspend fun deleteImage(image: Image) = imageRestApiService.deleteImage(image)

    override suspend fun updateImage(image: Image) = imageRestApiService.updateImage(image)
}