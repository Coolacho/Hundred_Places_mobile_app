package com.example.hundredplaces.data.model.usersPlacesPreferences.repositories

import android.util.Log
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesLocalDataSource
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesRemoteDataSource
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class UsersPlacesPreferencesDataRepository(
    private val usersPlacesPreferencesLocalDataSource: UsersPlacesPreferencesLocalDataSource,
    private val usersPlacesPreferencesRemoteDataSource: UsersPlacesPreferencesRemoteDataSource,
    private val networkConnection: NetworkConnection
) : UsersPlacesPreferencesRepository {

    override suspend fun pushUsersPlacesPreferences(userId: Long) {
        usersPlacesPreferencesLocalDataSource.getAllByUserId(userId)
            .collect { usersPlacesPreferences ->
                if (networkConnection.isNetworkConnected) {
                    usersPlacesPreferencesRemoteDataSource.insertAll(usersPlacesPreferences)
                }
            }
    }

    override suspend fun pullUsersPlacesPreferences(userId: Long) {
        usersPlacesPreferencesRemoteDataSource.getUsersPlacesPreferencesByUserId(userId)
            .catch { Log.e("UsersPlacesPreferences flow", "${it.message}") }
            .collect { usersPlacesPreferences ->
                val remoteIds = usersPlacesPreferences.map { it.placeId }
                usersPlacesPreferencesLocalDataSource.deleteUsersPlacesPreferencesNotIn(userId, remoteIds)
                usersPlacesPreferencesLocalDataSource.insertAll(usersPlacesPreferences)
                Log.d("Flows test", "$usersPlacesPreferences")
            }
    }

    override fun getFavoritePlacesByUserId(userId: Long): Flow<List<Long>> = usersPlacesPreferencesLocalDataSource.getFavoritePlacesByUserId(userId)
    override fun getPlacesRatingsByUserId(userId: Long): Flow<Map<Long, Double>> = usersPlacesPreferencesLocalDataSource.getPlacesRatingsByUserId(userId)

    override suspend fun upsert(usersPlacesPreferences: UsersPlacesPreferences) = usersPlacesPreferencesLocalDataSource.upsert(usersPlacesPreferences)

    override suspend fun delete(usersPlacesPreferences: UsersPlacesPreferences) = usersPlacesPreferencesLocalDataSource.delete(usersPlacesPreferences)

}