package com.example.hundredplaces.data.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestUserRepository: UserRepository {

    private val _userId = MutableStateFlow<Long?>(null)
    override val userId: StateFlow<Long?> = _userId

    /**
     * A test-only API to allow controlling the user id.
     */
    fun setUserId(id: Long?) {
        _userId.value = id
    }

    override fun logOut() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmailAndPassword(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun pullUser(): Pair<String, String>? {
        TODO("Not yet implemented")
    }

    override suspend fun insertUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserDetails(
        name: String,
        email: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPassword(
        oldPassword: String,
        newPassword: String
    ): Boolean {
        TODO("Not yet implemented")
    }
}