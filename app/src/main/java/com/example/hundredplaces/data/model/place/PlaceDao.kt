package com.example.hundredplaces.data.model.place

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(place: Place)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Query("SELECT * FROM places WHERE id = :id")
    fun getPlace(id: Int): Flow<Place>

    @Query("SELECT * FROM places ORDER BY id ASC")
    fun getAllPlaces(): Flow<List<Place>>
}