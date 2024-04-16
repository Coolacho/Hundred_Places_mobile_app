package com.example.hundredplaces.data.model.visit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(visit: Visit)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(visit: Visit)

    @Delete
    suspend fun delete(visit: Visit)

    @Query("SELECT * FROM visits WHERE id = :id")
    fun getVisit(id: Long): Flow<Visit>

    @Query("SELECT * FROM visits ORDER BY user_id ASC")
    fun getAllVisits(): Flow<List<Visit>>
}