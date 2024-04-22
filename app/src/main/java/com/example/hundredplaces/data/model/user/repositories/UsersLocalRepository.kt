package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserDao

class UsersLocalRepository(
    private val userDao: UserDao
) : UsersRepository{

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User = userDao.getUserByEmailAndPassword(email, password)
    override suspend fun getUserByEmail(email: String): User = userDao.getUserByEmail(email)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

    override suspend fun updateUser(user: User) = userDao.update(user)
}