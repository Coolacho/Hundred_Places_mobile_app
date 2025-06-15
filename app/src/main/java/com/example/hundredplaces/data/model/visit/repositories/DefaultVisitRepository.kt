package com.example.hundredplaces.data.model.visit.repositories

import android.util.Log
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.datasources.VisitDao
import com.example.hundredplaces.data.model.visit.datasources.VisitRestApi
import com.example.hundredplaces.util.NetworkMonitor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class DefaultVisitRepository(
    private val visitLocalDataSource: VisitDao,
    private val visitRemoteDataSource: VisitRestApi,
    private val networkConnection: NetworkMonitor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : VisitRepository{

    override fun getAllVisitDatesByUserIdAndPlaceId(userId: Long, placeId: Long) =
        visitLocalDataSource.getAllVisitDatesByUserIdAndByPlaceId(userId, placeId)

    override fun getAllVisitsCountByUserId(userId: Long): Flow<Int> =
        visitLocalDataSource.getAllVisitsCountByUserId(userId)

    override fun getVisitsCountByIsHundredPlacesAndUserId(userId: Long): Flow<Int> =
        visitLocalDataSource.getVisitsCountByIsHundredPlacesAndByUserId(userId)

    override fun getVisitsCountByPlaceTypeAndUserId(userId: Long): Flow<Map<PlaceTypeEnum, Int>> =
        visitLocalDataSource.getVisitsCountByPlaceTypeAndByUserId(userId)

    override suspend fun insertVisit(visit: Visit) : Boolean {
        val result = visitLocalDataSource.insert(visit)
        return result != -1L
    }

    override suspend fun pushVisits(userId: Long) {
        visitLocalDataSource.getAllVisitsByUserId(userId)
            .collect { visits ->
                if (visits.isNotEmpty()) {
                    if (networkConnection.isNetworkConnected) {
                        withContext(dispatcher) {
                            try {
                                visitRemoteDataSource.insertAll(visits)
                            } catch (_: SocketTimeoutException) {
                                Log.e("Visit Repository", "Server is unreachable.")
                            }
                        }
                    }
                }
            }
    }

    override suspend fun pullVisits(userId: Long) {
        withContext(dispatcher) {
            if (networkConnection.isNetworkConnected) {
                try {
                    val remoteVisits = visitRemoteDataSource.getAllVisitsByUserId(userId)
                    if (remoteVisits.isNotEmpty()) {
                        val remoteIds = remoteVisits.map { it.id }
                        visitLocalDataSource.deleteVisitsNotIn(remoteIds, userId)
                        visitLocalDataSource.insertAll(remoteVisits)
                        Log.d("Visit Repository", "$remoteVisits")
                    }
                }
                catch (_: SocketTimeoutException) {
                    Log.e("Visit Repository", "Server is unreachable.")
                }
            }
        }
    }

}