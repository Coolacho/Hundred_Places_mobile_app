package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.visit.Visit
import kotlinx.coroutines.flow.Flow
import java.time.Instant


/**
 * Repository that provides insert, update, delete, and retrieve of [Visit] from a given data source.
 */
interface VisitsRepository {
    /**
     * Retrieve all the visits for a given place by userId from the given data source.
     */
    fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long): Flow<List<Instant>>

    /**
     * Retrieve the number of all the visits for a given user from the given data source.
     */
    fun getAllVisitsCountByUserId(userId: Long): Flow<Int>

    /**
     * Retrieve the number of all the visits of places part of Hundred Places
     * for a given user from the given data source.
     */
    fun getVisitsCountByIsHundredPlacesAndUserId(userId: Long): Flow<Int>

    /**
     * Retrieve the number of all the visits grouped by place type for a given user from the given data source.
     */
    fun getVisitsCountByPlaceTypeAndUserId(userId: Long): Flow<Map<PlaceTypeEnum, Int>>

    /**
     * Insert visit in the data source
     */
    suspend fun insertVisit(visit: Visit)

    suspend fun pushVisits(userId: Long)

    suspend fun pullVisits(userId: Long)

}