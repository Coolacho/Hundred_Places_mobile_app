package com.example.hundredplaces.data.model.image.datasources

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.hundredplaces.data.model.image.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Upsert
    suspend fun insertAll(images: List<Image>)

    @Query("DELETE FROM images WHERE id NOT IN (:ids)")
    suspend fun deleteImagesNotIn(ids: List<Long>)

    @Transaction
    @Query("SELECT image_path " +
            "FROM (" +
            "   SELECT image_path," +
            "       ROW_NUMBER() OVER (PARTITION BY place_id) AS rn" +
            "   FROM images" +
            ") AS sub " +
            "WHERE rn = 1")
    fun getOneImagePerPlace(): Flow<List<String>>
}