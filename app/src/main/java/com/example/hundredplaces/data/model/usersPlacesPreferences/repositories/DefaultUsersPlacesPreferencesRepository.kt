package com.example.hundredplaces.data.model.usersPlacesPreferences.repositories

import android.util.Log
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesDao
import com.example.hundredplaces.data.model.usersPlacesPreferences.datasources.UsersPlacesPreferencesRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class DefaultUsersPlacesPreferencesRepository(
    private val usersPlacesPreferencesLocalDataSource: UsersPlacesPreferencesDao,
    private val usersPlacesPreferencesRemoteDataSource: UsersPlacesPreferencesRestApi,
    private val networkConnection: NetworkConnection,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersPlacesPreferencesRepository {

    override suspend fun pushUsersPlacesPreferences(userId: Long) {
        usersPlacesPreferencesLocalDataSource.getAllByUserId(userId)
            .collect { usersPlacesPreferences ->
                if (usersPlacesPreferences.isNotEmpty()) {
                    if (networkConnection.isNetworkConnected) {
                        withContext(dispatcher) {
                            try {
                                usersPlacesPreferencesRemoteDataSource.insertAll(usersPlacesPreferences)
                            }
                            catch (_: SocketTimeoutException) {
                                Log.e("UsersPlacesPreferences Repository", "Server is unreachable.")
                            }
                        }
                    }
                }
            }
    }

    override suspend fun pullUsersPlacesPreferences(userId: Long) {
        withContext(dispatcher) {
            if (networkConnection.isNetworkConnected) {
                try {
                    val remoteUsersPlacesPreferences = usersPlacesPreferencesRemoteDataSource.getUsersPlacesPreferencesByUserId(userId)
                    if (remoteUsersPlacesPreferences.isNotEmpty()) {
                        val remoteIds = remoteUsersPlacesPreferences.map { it.placeId }
                        usersPlacesPreferencesLocalDataSource.deleteUsersPlacesPreferencesNotIn(userId, remoteIds)
                        usersPlacesPreferencesLocalDataSource.insertAll(remoteUsersPlacesPreferences)
                        Log.d("UsersPlacesPreferences Repository", "$remoteUsersPlacesPreferences")
                    }
                }
                catch (_: SocketTimeoutException) {
                    Log.e("UsersPlacesPreferences Repository", "Server is unreachable.")
                }
            }
        }
    }

    override fun getFavoritePlacesByUserId(userId: Long): Flow<List<Long>> = usersPlacesPreferencesLocalDataSource.getFavoritePlacesByUserId(userId)
    override fun getPlacesRatingsByUserId(userId: Long): Flow<Map<Long, Double>> = usersPlacesPreferencesLocalDataSource.getPlacesRatingsByUserId(userId)

    override suspend fun upsert(usersPlacesPreferences: UsersPlacesPreferences) = usersPlacesPreferencesLocalDataSource.upsert(usersPlacesPreferences)

    override suspend fun delete(usersPlacesPreferences: UsersPlacesPreferences) = usersPlacesPreferencesLocalDataSource.delete(usersPlacesPreferences)

}