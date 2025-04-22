package com.example.hundredplaces.data.model.visit.repositories

import android.util.Log
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.datasources.VisitsLocalDataSource
import com.example.hundredplaces.data.model.visit.datasources.VisitsRemoteDataSource
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class VisitsDataRepository(
    private val visitsLocalDataSource: VisitsLocalDataSource,
    private val visitsRemoteDataSource: VisitsRemoteDataSource,
    private val networkConnection: NetworkConnection
) : VisitsRepository{

    override fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long) =
        visitsLocalDataSource.getAllVisitDatesByUserIdAndPlaceId(userId, placeId)

    override fun getAllVisitsCountByUserId(userId: Long): Flow<Int> =
        visitsLocalDataSource.getAllVisitsCountByUserId(userId)

    override fun getVisitsCountByIsHundredPlacesAndUserId(userId: Long): Flow<Int> =
        visitsLocalDataSource.getVisitsCountByIsHundredPlacesAndByUserId(userId)

    override fun getVisitsCountByPlaceTypeAndUserId(userId: Long): Flow<Map<PlaceTypeEnum, Int>> =
        visitsLocalDataSource.getVisitsCountByPlaceTypeAndByUserId(userId)

    override suspend fun insertVisit(visit: Visit) {
        visitsLocalDataSource.insert(visit)
    }

    override suspend fun pushVisits(userId: Long) {
        visitsLocalDataSource.getAllVisitsByUserId(userId)
            .collect { visits ->
                if (networkConnection.isNetworkConnected) {
                    visitsRemoteDataSource.insertVisits(visits)
                }
            }
    }

    override suspend fun pullVisits(userId: Long) {
        visitsRemoteDataSource.getAllVisitsByUserId(userId)
            .catch { Log.e("Visit flow", "${it.message}") }
            .collect { visits ->
                val remoteIds = visits.map { it.id }
                visitsLocalDataSource.deleteVisitsNotIn(remoteIds, userId)
                visitsLocalDataSource.insertAll(visits)
                Log.d("Flows test", "$visits")
            }
    }

}