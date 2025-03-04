package com.example.hundredplaces.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager

class WorkManagerRepository(
    context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    fun addGeofences() {
        workManager.beginUniqueWork(
            "geofence_adding_work",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<GeofenceWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
        ).enqueue()
    }

    fun sendNotification(placeId: Long) {
        workManager.beginUniqueWork(
            "notification_sending_work",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(
                    Data.Builder()
                        .putLong("PlaceId", placeId)
                        .build()
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        ).enqueue()
    }

    fun startSync(userId: Long) {
        workManager.beginUniqueWork(
            "sync_work",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<SyncWorker>()
                .setInputData(
                    Data.Builder()
                        .putLong("UserId", userId)
                        .build()
                )
                .build()
        ).enqueue()
    }

}