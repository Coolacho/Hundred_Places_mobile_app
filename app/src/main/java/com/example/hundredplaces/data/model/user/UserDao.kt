package com.example.hundredplaces.data.model.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: Int): Flow<User>

    @Query("SELECT * FROM users ORDER BY name ASC")
    suspend fun getAllUsers(): Flow<List<User>>
}