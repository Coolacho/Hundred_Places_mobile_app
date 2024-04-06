package com.example.hundredplaces.data.model.visit.repositories

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow

class VisitsDataRepository(
    private val visitsLocalRepository: VisitsLocalRepository,
    private val visitsRemoteRepository: VisitsRemoteRepository,
    private val networkConnection: NetworkConnection
) : VisitsRepository{
    override suspend fun getAllVisitsStream(): Flow<List<Visit>> {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.getAllVisitsStream()
        } else {
            visitsLocalRepository.getAllVisitsStream()
        }
    }

    override suspend fun getVisitStream(id: Int): Flow<Visit?> {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.getVisitStream(id)
        } else {
            visitsLocalRepository.getVisitStream(id)
        }
    }

    override suspend fun insertVisit(visit: Visit) {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.insertVisit(visit)
        } else {
            visitsLocalRepository.insertVisit(visit)
        }
    }

    override suspend fun deleteVisit(visit: Visit) {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.deleteVisit(visit)
        } else {
            visitsLocalRepository.deleteVisit(visit)
        }
    }

    override suspend fun updateVisit(visit: Visit) {
        return if (networkConnection.isNetworkConnected) {
            visitsRemoteRepository.updateVisit(visit)
        } else {
            visitsLocalRepository.updateVisit(visit)
        }
    }
}