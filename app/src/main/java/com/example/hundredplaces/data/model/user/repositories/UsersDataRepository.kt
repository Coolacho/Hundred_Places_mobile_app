package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserWithVisits
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow

class UsersDataRepository(
    private val usersLocalRepository: UsersLocalRepository,
    private val usersRemoteRepository: UsersRemoteRepository,
    private val networkConnection: NetworkConnection
) : UsersRepository{
    override suspend fun getAllUsersStream(): Flow<List<User>> {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.getAllUsersStream()
        } else {
            usersLocalRepository.getAllUsersStream()
        }
    }

    override suspend fun getUserStream(id: Long): Flow<User> {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.getUserStream(id)
        } else {
            usersLocalRepository.getUserStream(id)
        }
    }

    override suspend fun getUserWithVisitsStream(id: Long): Flow<UserWithVisits> {
        return if (networkConnection.isNetworkConnected) {
            usersRemoteRepository.getUserWithVisitsStream(id)
        } else {
            usersLocalRepository.getUserWithVisitsStream(id)
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