package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitDao
import kotlinx.coroutines.flow.Flow

class VisitsLocalRepository(
    private val visitDao: VisitDao
) : VisitsRepository{
    override suspend fun getAllVisitsStream(): Flow<List<Visit>> = visitDao.getAllVisits()

    override suspend fun getVisitStream(id: Int): Flow<Visit?> = visitDao.getVisit(id)

    override suspend fun insertVisit(visit: Visit) = visitDao.insert(visit)

    override suspend fun deleteVisit(visit: Visit) = visitDao.delete(visit)

    override suspend fun updateVisit(visit: Visit) = visitDao.update(visit)
}