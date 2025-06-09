package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository that provides insert, update, delete, and retrieve of [User] from a given data source.
 */
interface UserRepository {

    val userId: StateFlow<Long?>

    fun logOut()

    suspend fun getUserByEmailAndPassword(email: String, password: String)

    suspend fun getUserByEmail(email: String)

    suspend fun pullUser() : Pair<String, String>?

    /**
     * Insert user in the data source
     */
    suspend fun insertUser(user: User) : Boolean

    /**
     * Delete user from the data source
     */
    suspend fun deleteUser(user: User) : Boolean

    /**
     * Update user's details
     */
    suspend fun updateUserDetails(name: String, email: String) : Boolean

    /**
     * Update user's password
     */
    suspend fun updateUserPassword(oldPassword: String, newPassword: String): Boolean
}