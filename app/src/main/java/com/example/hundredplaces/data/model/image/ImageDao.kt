package com.example.hundredplaces.data.model.image

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(image: Image)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(image: Image)

    @Delete
    suspend fun delete(image: Image)

    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImage(id: Int): Flow<Image>

    @Query("SELECT * FROM images ORDER BY id ASC")
    suspend fun getAllImages(): Flow<List<Image>>
}