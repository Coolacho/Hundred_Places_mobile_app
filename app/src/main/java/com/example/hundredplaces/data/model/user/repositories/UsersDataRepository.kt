package com.example.hundredplaces.data.model.user.repositories

import android.util.Log
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.serialization.SerializationException
import java.net.SocketTimeoutException

class UsersDataRepository(
    private val usersLocalRepository: UsersLocalRepository,
    private val usersRemoteRepository: UsersRemoteRepository,
    private val networkConnection: NetworkConnection
) : UsersRepository{

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User {
        var user: User
        if (networkConnection.isNetworkConnected) {
            try {
                user = usersRemoteRepository.getUserByEmailAndPassword(email, password)
                usersLocalRepository.insertUser(user)
            }
            catch (e: SerializationException) {
                user = User(name ="", email = "", password = "")
                Log.d("UserRepository", "User is null")
            }
            catch (e: SocketTimeoutException) {
                user = usersLocalRepository.getUserByEmailAndPassword(email, password)
            }
        }
        else {
            user = usersLocalRepository.getUserByEmailAndPassword(email, password)
        }
        return user
    }

    override suspend fun getUserByEmail(email: String): User {
        var user: User
        if (networkConnection.isNetworkConnected) {
            try {
                user = usersRemoteRepository.getUserByEmail(email)
                usersLocalRepository.insertUser(user)
            }
            catch (e: SerializationException) {
                user = User(name ="", email = "", password = "")
                Log.d("UserRepository", "User is null")
            }
            catch (e: SocketTimeoutException) {
                user = usersLocalRepository.getUserByEmail(email)
            }
        } else {
            user = usersLocalRepository.getUserByEmail(email)
        }
        return user
    }

    override suspend fun insertUser(user: User) {
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteRepository.insertUser(user)
            } catch (e: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Insert in remote failed.")
            }
        }
        usersLocalRepository.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteRepository.deleteUser(user)
            } catch (e: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Delete in remote failed.")
            }
        }
        usersLocalRepository.deleteUser(user)
    }

    override suspend fun updateUser(user: User) {
        if (networkConnection.isNetworkConnected) {
            try {
                usersRemoteRepository.updateUser(user)
            } catch (e: SocketTimeoutException) {
                Log.e("Retrofit", "Server is not accessible. Update in remote failed.")
            }
        }
        usersLocalRepository.updateUser(user)
    }
}