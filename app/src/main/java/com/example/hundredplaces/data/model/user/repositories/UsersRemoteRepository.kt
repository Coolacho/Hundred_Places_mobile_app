package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserRestApiService
import kotlinx.coroutines.flow.Flow

class UsersRemoteRepository(
    private val userRestApiService: UserRestApiService
) : UsersRepository{
    override suspend fun getAllUsersStream(): Flow<List<User>> = userRestApiService.getAllUsers()

    override suspend fun getUserStream(id: Int): Flow<User?> = userRestApiService.getUser(id)

    override suspend fun insertUser(user: User) = userRestApiService.insertUser(user)

    override suspend fun deleteUser(user: User) = userRestApiService.deleteUser(user)

    override suspend fun updateUser(user: User) = userRestApiService.updateUser(user)
}