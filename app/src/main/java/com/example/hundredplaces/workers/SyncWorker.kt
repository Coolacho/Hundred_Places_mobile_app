package com.example.hundredplaces.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hundredplaces.data.model.city.repositories.CitiesRepository
import com.example.hundredplaces.data.model.image.repositories.ImagesRepository
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
import com.example.hundredplaces.data.model.usersPlacesPreferences.repositories.UsersPlacesPreferencesRepository
import com.example.hundredplaces.data.model.visit.repositories.VisitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val SYNC_WORK_TAG = "SyncWork"

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val citiesRepository: CitiesRepository,
    private val placesRepository: PlacesRepository,
    private val imagesRepository: ImagesRepository,
    private val usersPlacesPreferencesRepository: UsersPlacesPreferencesRepository,
    private val visitsRepository: VisitsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val userId = inputData.getLong("UserId", 0L)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                if (userId == 0L) {
                    throw IllegalArgumentException("Input data is null")
                }
                else {
                    //Update local data source
                    launch {
                        citiesRepository.pullCities()
                    }
                    delay(5000)
                    launch {
                        placesRepository.pullPlaces()
                    }
                    delay(5000)
                    launch {
                        imagesRepository.pullImages()
                    }

                    //Update remote data source
                    launch {
                        visitsRepository.pushVisits(userId)
                    }
                    launch {
                        usersPlacesPreferencesRepository.pushUsersPlacesPreferences(userId)
                    }

                    //Update local data source after pushing local updates
                    launch {
                        visitsRepository.pullVisits(userId)
                    }
                    launch {
                        usersPlacesPreferencesRepository.pullUsersPlacesPreferences(userId)
                    }

                    Log.d(SYNC_WORK_TAG, "Sync started successfully!")
                    Result.success()
                }
            }
            catch (e: IllegalArgumentException)
            {
                Log.e(SYNC_WORK_TAG, "${e.message}")
                Result.failure()
            }
            catch (e: Throwable)
            {
                Log.e(SYNC_WORK_TAG, "${e.message}")
                Result.failure()
            }
        }
    }
}