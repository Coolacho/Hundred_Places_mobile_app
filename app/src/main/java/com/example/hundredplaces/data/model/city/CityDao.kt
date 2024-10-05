package com.example.hundredplaces.data.model.city

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(city: City)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(city: City)

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCity(id: Long): City

    @Query("SELECT * FROM cities ORDER BY name ASC")
    suspend fun getAllCities(): List<City>
}