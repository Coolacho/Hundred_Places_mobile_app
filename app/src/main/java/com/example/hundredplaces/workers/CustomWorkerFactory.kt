package com.example.hundredplaces.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.hundredplaces.data.model.city.repositories.CitiesRepository
import com.example.hundredplaces.data.model.image.repositories.ImagesRepository
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitsRepository

class CustomWorkerFactory(
    private val citiesRepository: CitiesRepository,
    private val placesRepository: PlacesRepository,
    private val imagesRepository: ImagesRepository,
    private val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository,
    private val visitsRepository: VisitsRepository
) : WorkerFactory(){

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when(workerClassName) {
            GeofenceWorker::class.java.name ->
                GeofenceWorker(
                    appContext,
                    workerParameters,
                    placesRepository
                )
            SyncWorker::class.java.name ->
                SyncWorker(
                    appContext,
                    workerParameters,
                    citiesRepository,
                    placesRepository,
                    imagesRepository,
                    usersPlacesPreferencesRepository,
                    visitsRepository
                )
            else -> null
        }

    }
}