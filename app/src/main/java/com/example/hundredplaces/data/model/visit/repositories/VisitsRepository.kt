package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert, update, delete, and retrieve of [Visit] from a given data source.
 */
interface VisitsRepository {
    /**
     * Retrieve all the visits from the given data source.
     */
    suspend fun getAllVisitsStream(): Flow<List<Visit>>

    /**
     * Retrieve an visit from the given data source that matches with the [id].
     */
    suspend fun getVisitStream(id: Int): Flow<Visit?>

    /**
     * Insert visit in the data source
     */
    suspend fun insertVisit(visit: Visit)

    /**
     * Delete visit from the data source
     */
    suspend fun deleteVisit(visit: Visit)

    /**
     * Update visit in the data source
     */
    suspend fun updateVisit(visit: Visit)
}