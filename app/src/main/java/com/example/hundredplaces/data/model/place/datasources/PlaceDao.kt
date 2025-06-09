package com.example.hundredplaces.data.model.place.datasources

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.hundredplaces.data.model.place.Place
import com.example.hundredplaces.data.model.place.PlaceCoordinates
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Upsert
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM places WHERE id NOT IN (:ids)")
    suspend fun deletePlacesNotIn(ids: List<Long>)

    @Transaction
    @Query("SELECT id, latitude, longitude FROM places")
    fun getPlacesCoordinates(): List<PlaceCoordinates>

    @Transaction
    @Query("SELECT * FROM places")
    fun getAllPlaces(): Flow<List<Place>>

    @Transaction
    @Query("SELECT * FROM places WHERE id = :placeId")
    fun getPlaceWithCityAndImages(placeId: Long): Flow<PlaceWithCityAndImages?>

    @Transaction
    @Query("SELECT * FROM places")
    fun getAllPlacesWithCityAndImages(): Flow<List<PlaceWithCityAndImages>>
}