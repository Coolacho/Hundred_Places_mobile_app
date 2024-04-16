package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitRestApiService
import kotlinx.coroutines.flow.Flow

class VisitsRemoteRepository(
    private val visitRestApiService: VisitRestApiService
) : VisitsRepository{
    override suspend fun getAllVisitsStream(): Flow<List<Visit>> = visitRestApiService.getAllVisits()

    override suspend fun getVisitStream(id: Long): Flow<Visit?> = visitRestApiService.getVisit(id)

    override suspend fun insertVisit(visit: Visit) = visitRestApiService.insertVisit(visit)

    override suspend fun deleteVisit(visit: Visit) = visitRestApiService.deleteVisit(visit)

    override suspend fun updateVisit(visit: Visit) = visitRestApiService.updateVisit(visit)
}