package com.example.hundredplaces.data.model.user.datasources

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UsersRestApi

class UsersRemoteDataSource(
    private val usersRestApi: UsersRestApi
) {

    suspend fun getUserByEmailAndPassword(email: String, password: String): User = usersRestApi.getUserByEmailAndPassword(email, password)
    suspend fun getUserByEmail(email: String): User = usersRestApi.getUserByEmail(email)

    suspend fun insertUser(user: User) = usersRestApi.insertUser(user)

    suspend fun deleteUser(user: User) = usersRestApi.deleteUser(user)

    suspend fun updateUser(user: User) = usersRestApi.updateUser(user)
}