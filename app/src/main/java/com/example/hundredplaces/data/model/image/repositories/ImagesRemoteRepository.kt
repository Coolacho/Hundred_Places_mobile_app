package com.example.hundredplaces.data.model.image.repositories

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.ImageRestApiService
import kotlinx.coroutines.flow.Flow

class ImagesRemoteRepository(
    private val imageRestApiService: ImageRestApiService
) : ImagesRepository{
    override suspend fun getAllImagesStream(): Flow<List<Image>> = imageRestApiService.getAllImages()

    override suspend fun getImageStream(id: Int): Flow<Image?> = imageRestApiService.getImage(id)

    override suspend fun insertImage(image: Image) = imageRestApiService.insertImage(image)

    override suspend fun deleteImage(image: Image) = imageRestApiService.deleteImage(image)

    override suspend fun updateImage(image: Image) = imageRestApiService.updateImage(image)
}