package com.example.hundredplaces.data.model.place

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(place: Place)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Query("SELECT * FROM places WHERE id = :id")
    fun getPlace(id: Long): Place

    @Query("SELECT * FROM places ORDER BY id ASC")
    fun getAllPlaces(): List<Place>

    @Transaction
    @Query("SELECT * FROM places WHERE id = :id")
    fun getPlaceWithCityAndImages(id: Long): PlaceWithCityAndImages
    @Transaction
    @Query("SELECT * FROM places ORDER BY id ASC")
    fun getAllPlacesWithCityAndImages(): List<PlaceWithCityAndImages>

}