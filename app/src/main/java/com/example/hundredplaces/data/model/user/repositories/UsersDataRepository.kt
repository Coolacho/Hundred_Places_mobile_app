package com.example.hundredplaces.data.model.user.repositories

import android.util.Log
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.datasources.UsersLocalDataSource
import com.example.hundredplaces.data.model.user.datasources.UsersRemoteDataSource
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException

class UsersDataRepository(
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val networkConnection: NetworkConnection
) : UsersRepository{

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User {
        var user: User
        if (networkConnection.isNetworkConnected) {
            try {
                user = usersRemoteDataSource.getUserByEmailAndPassword(email, password)
                usersLocalDataSource.insertUser(user)
            }
            catch (e: SerializationException) {
                user = User(name ="", email = "", password = "")
                Log.d("UserRepository", "User is null")
            }
            catch (e: SocketTimeoutException) {
                user = usersLocalDataSource.getUserByEmailAndPassword(email, password)
            }
        }
        else {
            user = usersLocalDataSource.getUserByEmailAndPassword(email, password)
        }
        return user
    }

    override suspend fun getUserByEmail(email: String): User {
        var user: User
        if (networkConnection.isNetworkConnected) {
            try {
                user = usersRemoteDataSource.getUserByEmail(email)
                usersLocalDataSource.insertUser(user)
            }
            catch (e: SerializationException) {
                user = User(name ="", email = "", password = "")
                Log.d("UserRepository", "User is null")
            }
            catch (e: SocketTimeoutException) {
                user = usersLocalDataSource.getUserByEmail(email)
            }
        } else {
            user = usersLocalDataSource.getUserByEmail(email)
        }
        return user
    }

    override suspend fun insertUser(user: User) {
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteDataSource.insertUser(user)
            } catch (e: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Insert in remote failed.")
            }
        }
        usersLocalDataSource.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteDataSource.deleteUser(user)
            } catch (e: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Delete in remote failed.")
            }
        }
        usersLocalDataSource.deleteUser(user)
    }

    override suspend fun updateUser(user: User) {
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteDataSource.updateUser(user)
            } catch (e: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Update in remote failed.")
            }
        }
        usersLocalDataSource.updateUser(user)
    }
}