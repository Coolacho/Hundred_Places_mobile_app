package com.example.hundredplaces.data.model.image.repositories

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.ImageDao

class ImagesLocalRepository(
    private val imageDao: ImageDao
) : ImagesRepository{
    override suspend fun getAllImages(): List<Image> = imageDao.getAllImages()

    override suspend fun getImage(id: Long): Image = imageDao.getImage(id)

    override suspend fun insertImage(image: Image) = imageDao.insert(image)

    override suspend fun deleteImage(image: Image) = imageDao.delete(image)

    override suspend fun updateImage(image: Image) = imageDao.update(image)
}