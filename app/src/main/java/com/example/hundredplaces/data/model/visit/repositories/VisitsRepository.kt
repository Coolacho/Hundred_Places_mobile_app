package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import java.time.LocalDateTime


/**
 * Repository that provides insert, update, delete, and retrieve of [Visit] from a given data source.
 */
interface VisitsRepository {
    /**
     * Retrieve all the visits from the given data source.
     */
    suspend fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long): List<LocalDateTime>

    /**
     * Insert visit in the data source
     */
    suspend fun insertVisit(visit: Visit)

}