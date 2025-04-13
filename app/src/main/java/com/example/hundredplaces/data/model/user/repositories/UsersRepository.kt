package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User

/**
 * Repository that provides insert, update, delete, and retrieve of [User] from a given data source.
 */
interface UsersRepository {

    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    suspend fun getUserByEmail(email: String): User?

    /**
     * Insert user in the data source
     */
    suspend fun insertUser(user: User) : Boolean

    /**
     * Delete user from the data source
     */
    suspend fun deleteUser(user: User) : Boolean

    /**
     * Update user in the data source
     */
    suspend fun updateUser(user: User) : Boolean
}