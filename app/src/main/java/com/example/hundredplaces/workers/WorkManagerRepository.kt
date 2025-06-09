package com.example.hundredplaces.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerRepository(
    context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    fun addGeofences() {
        workManager.enqueue(
            PeriodicWorkRequestBuilder<GeofenceAddingWorker>(4, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .addTag("geofence_adding_work")
                .build()
        )
    }

    fun removeGeofence(placeId: String) {
        workManager.beginUniqueWork(
            "geofence_removing_work",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<GeofenceRemovingWorker>()
                .setInputData(
                    Data.Builder()
                        .putString("PlaceId", placeId)
                        .build()
                )
                .build()
        ).enqueue()
    }

}