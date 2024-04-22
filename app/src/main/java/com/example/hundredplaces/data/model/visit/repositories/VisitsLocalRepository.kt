package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitDao
import java.time.LocalDateTime

class VisitsLocalRepository(
    private val visitDao: VisitDao
) : VisitsRepository{
    override suspend fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long): List<LocalDateTime> = visitDao.getAllVisitsByUserIdAndPlaceId(userId, placeId)

    override suspend fun insertVisit(visit: Visit) = visitDao.insert(visit)
}