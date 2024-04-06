package com.example.hundredplaces.data.model.user.repositories

import com.example.hundredplaces.data.model.user.User
import com.example.hundredplaces.data.model.user.UserDao
import kotlinx.coroutines.flow.Flow

class UsersLocalRepository(
    private val userDao: UserDao
) : UsersRepository{
    override suspend fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    override suspend fun getUserStream(id: Int): Flow<User?> = userDao.getUser(id)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

    override suspend fun updateUser(user: User) = userDao.update(user)
}