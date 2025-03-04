package com.example.hundredplaces.data.model.usersPlacesPreferences.repositories

import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import kotlinx.coroutines.flow.Flow

interface UsersPlacesPreferencesRepository {

    suspend fun pushUsersPlacesPreferences(userId: Long)

    suspend fun pullUsersPlacesPreferences(userId: Long)

    fun getFavoritePlacesByUserId(userId: Long): Flow<List<Long>>

    fun getPlacesRatingsByUserId(userId: Long): Flow<Map<Long, Double>>

    suspend fun upsert(usersPlacesPreferences: UsersPlacesPreferences)

    suspend fun delete(usersPlacesPreferences: UsersPlacesPreferences)
}