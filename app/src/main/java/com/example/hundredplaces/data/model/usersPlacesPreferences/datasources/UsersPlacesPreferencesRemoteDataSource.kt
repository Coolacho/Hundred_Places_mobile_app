package com.example.hundredplaces.data.model.usersPlacesPreferences.datasources

import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferences
import com.example.hundredplaces.data.model.usersPlacesPreferences.UsersPlacesPreferencesRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class UsersPlacesPreferencesRemoteDataSource(
    private val usersPlacesPreferencesRestApi: UsersPlacesPreferencesRestApi,
    private val networkConnection: NetworkConnection,
    private val refreshIntervalMs: Long = 60000
) {

    fun getUsersPlacesPreferencesByUserId(userId: Long) = flow {
        while (true) {
            if (networkConnection.isNetworkConnected) {
                val allUsersPlacesPreferences = usersPlacesPreferencesRestApi.getUsersPlacesPreferencesByUserId(userId)
                emit(allUsersPlacesPreferences)
                delay(refreshIntervalMs)
            }
        }
    }

    suspend fun insertAll(usersPlacesPreferences: List<UsersPlacesPreferences>) =
        usersPlacesPreferencesRestApi.insertAll(usersPlacesPreferences)
}