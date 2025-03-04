package com.example.hundredplaces.data.model.visit

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.util.UUID

@Dao
interface VisitDao {
    /**
     * Wrapper function for inserting a single visit.
     */
    @Transaction
    suspend fun insert(visit: Visit) : Long {
        return insert(visit.id, visit.userId, visit.placeId, visit.dateVisited)
    }

    /**
     * Inserts a single visit by checking if there isn't one for the same place by the same user on the same day
     */
    @Query("INSERT INTO visits " +
            "SELECT :id, :userId, :placeId, :dateVisited " +
            "WHERE NOT EXISTS (" +
            "SELECT 1 " +
            "FROM visits " +
            "WHERE user_id = :userId " +
            "AND place_id = :placeId " +
            "AND DATE(date_visited) = DATE(:dateVisited)" +
            ");")
    suspend fun insert(id: UUID,userId: Long, placeId: Long, dateVisited: Instant): Long

    /**
     * Inserts all visits in the list. Made for batch insert when syncing with a remote datasource, where the ladder overwrites the local datasource.
     */
    @Upsert
    suspend fun insertAll(visits: List<Visit>)

    @Query("DELETE FROM visits WHERE id NOT IN (:ids)")
    suspend fun deleteVisitsNotIn(ids: List<UUID>)

    @Transaction
    @Query("SELECT * FROM visits WHERE user_id = :userId")
    fun getAllVisitsByUserId(userId: Long): Flow<List<Visit>>

    @Transaction
    @Query("SELECT date_visited FROM visits WHERE user_id = :userId AND place_id = :placeId")
    fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long): Flow<List<Instant>>

    @Transaction
    @Query("SELECT COUNT(visits.id) FROM visits LEFT JOIN places ON visits.place_id = places.id WHERE visits.user_id = :userId AND places.is_100_places")
    fun getVisitsCountByIsHundredPlacesAndByUserId(userId: Long): Flow<Int>

    @Transaction
    @Query("SELECT places.type, COUNT(visits.id) AS visitsCount FROM visits LEFT JOIN places ON visits.place_id = places.id WHERE visits.user_id = :userId GROUP BY places.type")
    fun getVisitsCountByPlaceTypeAndUserId(userId: Long): Flow<Map<@MapColumn(columnName = "type")PlaceTypeEnum, @MapColumn(columnName = "visitsCount")Int>>

    @Transaction
    @Query("SELECT COUNT(visits.id) FROM visits LEFT JOIN places ON visits.place_id = places.id WHERE visits.user_id = :userId")
    fun getAllVisitsCountByUserId(userId: Long): Flow<Int>


}