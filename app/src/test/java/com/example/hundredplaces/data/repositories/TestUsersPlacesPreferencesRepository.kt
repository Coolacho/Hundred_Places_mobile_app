package com.example.hundredplaces.data.repositories

import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import kotlinx.coroutines.flow.Flow

class TestUsersPlacesPreferencesRepository: UsersPlacesPreferencesRepository {
    override suspend fun pushUsersPlacesPreferences(userId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun pullUsersPlacesPreferences(userId: Long) {
        TODO("Not yet implemented")
    }

    override fun getFavoritePlacesByUserId(userId: Long): Flow<List<Long>> {
        TODO("Not yet implemented")
    }

    override fun getPlacesRatingsByUserId(userId: Long): Flow<Map<Long, Double>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsert(usersPlacesPreferences: UsersPlacesPreferences) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(usersPlacesPreferences: UsersPlacesPreferences) {
        TODO("Not yet implemented")
    }
}