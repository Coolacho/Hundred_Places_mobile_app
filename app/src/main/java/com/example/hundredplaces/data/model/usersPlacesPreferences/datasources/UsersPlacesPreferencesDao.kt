package com.example.hundredplaces.data.model.usersPlacesPreferences.datasources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersPlacesPreferencesDao {

    @Upsert
    suspend fun upsert(usersPlacesPreferences: UsersPlacesPreferences)

    @Upsert
    suspend fun insertAll(usersPlacesPreferences: List<UsersPlacesPreferences>)

    @Delete
    suspend fun delete(usersPlacesPreferences: UsersPlacesPreferences)

    @Query("DELETE FROM users_places_preferences WHERE user_id = :userId AND place_id NOT IN (:ids)")
    suspend fun deleteUsersPlacesPreferencesNotIn(userId: Long, ids: List<Long>)

    @Transaction
    @Query("SELECT place_id FROM users_places_preferences WHERE user_id = :userId AND is_favorite = 1")
    fun getFavoritePlacesByUserId(userId: Long) : Flow<List<Long>>

    @Transaction
    @Query("SELECT place_id, rating FROM users_places_preferences WHERE user_id = :userId")
    fun getPlacesRatingsByUserId(userId: Long): Flow<Map<@MapColumn(columnName = "place_id")Long, @MapColumn(columnName = "rating")Double>>

    @Transaction
    @Query("SELECT * FROM users_places_preferences WHERE user_id = :userId")
    fun getAllByUserId(userId: Long): Flow<List<UsersPlacesPreferences>>
}