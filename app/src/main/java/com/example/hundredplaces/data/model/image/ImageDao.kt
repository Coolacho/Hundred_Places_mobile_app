package com.example.hundredplaces.data.model.image

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(image: Image)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(image: Image)

    @Delete
    suspend fun delete(image: Image)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getImage(id: Long): Image

    @Query("SELECT * FROM images ORDER BY id ASC")
    fun getAllImages(): List<Image>
}