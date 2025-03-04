package com.example.hundredplaces.data.model.image.datasources

import com.example.hundredplaces.data.model.image.Image
import com.example.hundredplaces.data.model.image.ImageDao

class ImagesLocalDataSource(
    private val imageDao: ImageDao
) {
    val allImages = imageDao.getAll()

    suspend fun insertAll(images: List<Image>) = imageDao.insertAll(images)

    suspend fun deleteImagesNotIn(ids: List<Long>) = imageDao.deleteImagesNotIn(ids)
}