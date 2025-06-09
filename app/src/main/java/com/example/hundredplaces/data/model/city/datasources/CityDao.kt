package com.example.hundredplaces.data.model.city.datasources

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.hundredplaces.data.model.city.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Upsert
    suspend fun insertAll(cities: List<City>)

    @Query("DELETE FROM cities WHERE id NOT IN (:ids)")
    suspend fun deleteCitiesNotIn(ids: List<Long>)

    @Transaction
    @Query("SELECT * FROM cities")
    fun getAll(): Flow<List<City>>

}