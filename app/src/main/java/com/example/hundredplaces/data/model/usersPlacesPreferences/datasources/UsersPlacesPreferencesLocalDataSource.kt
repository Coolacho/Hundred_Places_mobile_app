package com.example.hundredplaces.data.model.usersPlacesPreferences.datasources

import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferencesDao

class UsersPlacesPreferencesLocalDataSource(
    private val usersPlacesPreferencesDao: UsersPlacesPreferencesDao
)  {
    fun getAllByUserId(userId: Long) = usersPlacesPreferencesDao.getAllByUserId(userId)

    fun getFavoritePlacesByUserId(userId: Long) = usersPlacesPreferencesDao.getFavoritePlacesByUserId(userId)

    fun getPlacesRatingsByUserId(userId: Long) = usersPlacesPreferencesDao.getPlacesRatingsByUserId(userId)

    suspend fun upsert(usersPlacesPreferences: UsersPlacesPreferences) = usersPlacesPreferencesDao.upsert(usersPlacesPreferences)

    suspend fun insertAll(usersPlacesPreferences: List<UsersPlacesPreferences>) = usersPlacesPreferencesDao.insertAll(usersPlacesPreferences)

    suspend fun delete(usersPlacesPreferences: UsersPlacesPreferences) = usersPlacesPreferencesDao.delete(usersPlacesPreferences)

    suspend fun deleteUsersPlacesPreferencesNotIn(userId: Long, ids: List<Long>) = usersPlacesPreferencesDao.deleteUsersPlacesPreferencesNotIn(userId, ids)
}