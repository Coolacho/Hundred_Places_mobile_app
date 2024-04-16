package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserWithVisits
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [User] from a given data source.
 */
interface UsersRepository {
    /**
     * Retrieve all the users from the given data source.
     */
    suspend fun getAllUsersStream(): Flow<List<User>>

    /**
     * Retrieve an user from the given data source that matches with the [id].
     */
    suspend fun getUserStream(id: Long): Flow<User>

    /**
     * Retrieve an user with visits from the given data source that matches with the [id].
     */
    suspend fun getUserWithVisitsStream(id: Long): Flow<UserWithVisits>

    /**
     * Insert user in the data source
     */
    suspend fun insertUser(user: User)

    /**
     * Delete user from the data source
     */
    suspend fun deleteUser(user: User)

    /**
     * Update user in the data source
     */
    suspend fun updateUser(user: User)
}