package com.example.hundredplaces.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.services.distance.DistanceService

class CustomWorkerFactory(
    private val placeRepository: PlaceRepository,
    private val distanceService: DistanceService
) : WorkerFactory(){

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when(workerClassName) {
            GeofenceAddingWorker::class.java.name ->
                GeofenceAddingWorker(
                    appContext,
                    workerParameters,
                    placeRepository,
                    distanceService
                )
            else -> null
        }

    }
}