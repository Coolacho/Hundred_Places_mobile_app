package com.example.hundredplaces.data.model.image

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Upsert
    suspend fun insertAll(images: List<Image>)

    @Query("DELETE FROM images WHERE id NOT IN (:ids)")
    suspend fun deleteImagesNotIn(ids: List<Long>)

    @Transaction
    @Query("SELECT * FROM images")
    fun getAll(): Flow<List<Image>>
}