package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitRestApiService
import java.time.LocalDateTime

class VisitsRemoteRepository(
    private val visitRestApiService: VisitRestApiService
) : VisitsRepository{
    override suspend fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long): List<LocalDateTime> = visitRestApiService.getAllVisitDatesByUserIdAndPlaceId(userId, placeId)

    override suspend fun insertVisit(visit: Visit) = visitRestApiService.insertVisit(visit)
}