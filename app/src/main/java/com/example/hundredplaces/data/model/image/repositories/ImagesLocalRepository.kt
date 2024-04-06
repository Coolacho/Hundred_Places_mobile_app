package com.example.hundredplaces.data.model.image.repositories

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.ImageDao
import kotlinx.coroutines.flow.Flow

class ImagesLocalRepository(
    private val imageDao: ImageDao
) : ImagesRepository{
    override suspend fun getAllImagesStream(): Flow<List<Image>> = imageDao.getAllImages()

    override suspend fun getImageStream(id: Int): Flow<Image?> = imageDao.getImage(id)

    override suspend fun insertImage(image: Image) = imageDao.insert(image)

    override suspend fun deleteImage(image: Image) = imageDao.delete(image)

    override suspend fun updateImage(image: Image) = imageDao.update(image)
}