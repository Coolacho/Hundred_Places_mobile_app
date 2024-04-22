package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserRestApiService

class UsersRemoteRepository(
    private val userRestApiService: UserRestApiService
) : UsersRepository{

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User = userRestApiService.getUserByEmailAndPassword(email, password)
    override suspend fun getUserByEmail(email: String): User = userRestApiService.getUserByEmail(email)

    override suspend fun insertUser(user: User) = userRestApiService.insertUser(user)

    override suspend fun deleteUser(user: User) = userRestApiService.deleteUser(user)

    override suspend fun updateUser(user: User) = userRestApiService.updateUser(user)
}