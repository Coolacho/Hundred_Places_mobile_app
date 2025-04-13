package com.example.hundredplaces.data.model.user.datasources

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserDao

class UsersLocalDataSource(
    private val userDao: UserDao
) {

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? = userDao.getUserByEmailAndPassword(email, password)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)

    suspend fun insertUser(user: User) = userDao.insert(user)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun updateUser(user: User) = userDao.update(user)
}