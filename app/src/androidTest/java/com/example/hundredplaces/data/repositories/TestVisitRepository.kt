package com.example.hundredplaces.data.repositories

import android.util.Log
import com.example.hundredplaces.data.model.place.PlaceTypeEnum
import com.example.hundredplaces.data.model.visit.Visit
import com.example.hundredplaces.data.model.visit.repositories.VisitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.jetbrains.annotations.TestOnly
import java.time.Instant

class TestVisitRepository: VisitRepository {

    private val _visits = MutableStateFlow(emptyList<Visit>())
    val visits: StateFlow<List<Visit>> = _visits

    override fun getAllVisitDatesByUserIdAndPlaceId(
        userId: Long,
        placeId: Long
    ): Flow<List<Instant>> = _visits.map { visitsList ->
        visitsList
            .filter { visit -> visit.userId == userId && visit.placeId == placeId }
            .map { visit -> visit.dateVisited }
    }

    override fun getAllVisitsCountByUserId(userId: Long): Flow<Int> = _visits.map { visitsList ->
        visitsList
            .filter { visit -> visit.userId == userId }
            .size
    }

    override fun getVisitsCountByIsHundredPlacesAndUserId(userId: Long): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getVisitsCountByPlaceTypeAndUserId(userId: Long): Flow<Map<PlaceTypeEnum, Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertVisit(visit: Visit): Boolean {
        Log.d("CAMERA", "ADDING VISIT")
        _visits.update { it + visit }
        return true
    }

    override suspend fun pushVisits(userId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun pullVisits(userId: Long) {
        TODO("Not yet implemented")
    }

    @TestOnly
    fun addVisits(visits: List<Visit>) = _visits.update { it + visits }

    @TestOnly
    fun deleteAllVisits() { _visits.value = emptyList<Visit>() }
}