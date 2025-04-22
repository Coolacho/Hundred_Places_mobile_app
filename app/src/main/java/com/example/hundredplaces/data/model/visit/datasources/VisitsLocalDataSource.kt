package com.example.hundredplaces.data.model.visit.datasources

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitDao
import java.util.UUID

class VisitsLocalDataSource(
    private val visitDao: VisitDao
) {
    suspend fun insert(visit: Visit) = visitDao.insert(visit)

    suspend fun insertAll(visits: List<Visit>) = visitDao.insertAll(visits)

    suspend fun deleteVisitsNotIn(ids: List<UUID>, userId: Long) = visitDao.deleteVisitsNotIn(ids, userId)

    fun getAllVisitsByUserId(userId: Long) = visitDao.getAllVisitsByUserId(userId)

    fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long) = visitDao.getAllVisitDatesByUserIdAndPlaceId(userId, placeId)

    fun getVisitsCountByIsHundredPlacesAndByUserId(userId: Long) = visitDao.getVisitsCountByIsHundredPlacesAndByUserId(userId)

    fun getVisitsCountByPlaceTypeAndByUserId(userId: Long) = visitDao.getVisitsCountByPlaceTypeAndUserId(userId)

    fun getAllVisitsCountByUserId(userId: Long) = visitDao.getAllVisitsCountByUserId(userId)

}