package com.example.hundredplaces.data.model.visit.datasources

import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.VisitsRestApi
import com.example.hundredplaces.util.NetworkConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VisitsRemoteDataSource(
    private val visitsRestApi: VisitsRestApi,
    private val networkConnection: NetworkConnection,
    private val refreshIntervalMs: Long = 60000
) {
    fun getAllVisitsByUserId(userId: Long): Flow<List<Visit>> = flow {
        while (true) {
            if (networkConnection.isNetworkConnected) {
                val visits = visitsRestApi.getAllVisitsByUserId(userId)
                emit(visits)
                delay(refreshIntervalMs)
            }
        }
    }

    suspend fun insertVisits(visits: List<Visit>) = visitsRestApi.insertAll(visits)
}