package com.example.hundredplaces.data.model.visit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(visit: Visit)

    @Query("SELECT date_visited FROM visits WHERE user_id = :userId AND place_id = :placeId")
    fun getAllVisitsByUserIdAndPlaceId(userId: Long, placeId: Long): List<LocalDateTime>
}