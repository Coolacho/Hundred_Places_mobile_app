package com.example.hundredplaces.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hundredplaces.data.model.place.PlaceWithCityAndImages
import com.example.hundredplaces.util.GeofenceWorker
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WorkManagerRepository(
    context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    fun addGeofences(places: List<PlaceWithCityAndImages>) {
        workManager.beginUniqueWork(
            "geofence_adding_work",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<GeofenceWorker>()
                .setInputData(createInputData(places))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
        ).enqueue()
    }

    private fun createInputData(places: List<PlaceWithCityAndImages>) : Data {
        val placesCoordinates = mutableMapOf<Long, Pair<Double, Double>>()
        places.forEach {
            placesCoordinates[it.place.id] = Pair(it.place.latitude, it.place.longitude)
        }
        val placesCoordinatesString = Json.encodeToString(placesCoordinates)
        return Data.Builder()
            .putString("PLACES_COORDINATES", placesCoordinatesString)
            .build()
    }

}