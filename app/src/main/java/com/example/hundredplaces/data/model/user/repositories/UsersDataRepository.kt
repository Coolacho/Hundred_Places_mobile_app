package com.example.hundredplaces.data.model.user.repositories

import android.util.Log
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.datasources.UsersLocalDataSource
import com.example.hundredplaces.data.model.user.datasources.UsersRemoteDataSource
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException

const val USER_REPOSITORY_TAG = "userDataRepository"

class UsersDataRepository(
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val networkConnection: NetworkConnection
) : UsersRepository{

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User? {

        var user: User? = null

        user = usersLocalDataSource.getUserByEmailAndPassword(email, password)

        if (networkConnection.isNetworkConnected && user == null) {
            try {
                user = usersRemoteDataSource.getUserByEmailAndPassword(email, password)
                if (user != null) usersLocalDataSource.insertUser(user)
            }
            catch (_: SerializationException) {
                Log.e(USER_REPOSITORY_TAG, "Serialization error occurred!")
            }
            catch (_: SocketTimeoutException) {
                Log.e(USER_REPOSITORY_TAG, "Server is not reachable!")
            }
        }

        return user
    }

    override suspend fun getUserByEmail(email: String): User? {

        var user: User? = null

        user = usersLocalDataSource.getUserByEmail(email)

        if (networkConnection.isNetworkConnected && user == null) {
            try {
                user = usersRemoteDataSource.getUserByEmail(email)
                if (user != null) usersLocalDataSource.insertUser(user)
            }
            catch (_: SerializationException) {
                Log.e(USER_REPOSITORY_TAG, "Serialization error occurred!")
            }
            catch (_: SocketTimeoutException) {
                Log.e(USER_REPOSITORY_TAG, "Server is not reachable!")
            }
        }

        return user
    }

    override suspend fun insertUser(user: User) : Boolean {
        var result = false
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteDataSource.insertUser(user)
                usersLocalDataSource.insertUser(user)
                result = true
            } catch (_: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Insert in remote failed.")
            }
        }
        return result
    }

    override suspend fun deleteUser(user: User) : Boolean {
        var result = false
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteDataSource.deleteUser(user)
                usersLocalDataSource.deleteUser(user)
                result = true
            } catch (_: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Delete in remote failed.")
            }
        }
        return result
    }

    override suspend fun updateUser(user: User) : Boolean {
        var result = false
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteDataSource.updateUser(user)
                usersLocalDataSource.updateUser(user)
                result = true
            } catch (_: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Update in remote failed.")
            }
        }
        return result
    }
}