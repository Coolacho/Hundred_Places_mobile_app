package com.example.hundredplaces.data.services.distance

import android.location.Location
import kotlinx.coroutines.flow.StateFlow

interface DistanceService {

    val distances: StateFlow<Map<Long, Float>>

    fun getDistances(location: Location)
}