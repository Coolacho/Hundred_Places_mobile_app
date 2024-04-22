package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.util.NetworkConnection
import java.time.LocalDateTime

class VisitsDataRepository(
    private val visitsLocalRepository: VisitsLocalRepository,
    private val visitsRemoteRepository: VisitsRemoteRepository,
    private val networkConnection: NetworkConnection
) : VisitsRepository{
    override suspend fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long): List<LocalDateTime> {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.getAllVisitDatesByUserIdAndPlaceId(userId, placeId)
        } else {
            visitsLocalRepository.getAllVisitDatesByUserIdAndPlaceId(userId, placeId)
        }
    }

    override suspend fun insertVisit(visit: Visit) {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.insertVisit(visit)
        } else {
            visitsLocalRepository.insertVisit(visit)
        }
    }

}