package com.example.hundredplaces.data.model.user.repositories

import android.util.Log
import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.serialization.SerializationException

class UsersDataRepository(
    private val usersLocalRepository: UsersLocalRepository,
    private val usersRemoteRepository: UsersRemoteRepository,
    private val networkConnection: NetworkConnection
) : UsersRepository{

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User {
        var user: User
        try {
            user = usersRemoteRepository.getUserByEmailAndPassword(email, password)
            usersLocalRepository.insertUser(user)
        }
        catch (e: SerializationException) {
            user = User(name ="", email = "", password = "")
            Log.d("UserRepository", "User is null")
        }
        return user
    }

    override suspend fun getUserByEmail(email: String): User {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.getUserByEmail(email)
        } else {
            usersLocalRepository.getUserByEmail(email)
        }
    }

    override suspend fun insertUser(user: User) {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.insertUser(user)
        } else {
            usersLocalRepository.insertUser(user)
        }
    }

    override suspend fun deleteUser(user: User) {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.deleteUser(user)
        } else {
            usersLocalRepository.deleteUser(user)
        }
    }

    override suspend fun updateUser(user: User) {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.updateUser(user)
        } else {
            usersLocalRepository.updateUser(user)
        }
    }
}