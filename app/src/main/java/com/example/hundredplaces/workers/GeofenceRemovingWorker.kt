package com.example.hundredplaces.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.api.gax.rpc.ApiException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val GEOFENCE_REMOVE_WORK_TAG = "GeofenceRemovingWorker"

class GeofenceRemovingWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        val placeId = inputData.getString("PlaceId")

        return try {
            if (placeId.isNullOrEmpty()) {
                throw IllegalArgumentException("Input data is null!")
            }
            else {
                val geofencingClient = LocationServices.getGeofencingClient(applicationContext)

                suspendCoroutine<Unit> { cont ->
                    geofencingClient.removeGeofences(listOf(placeId.toString())).run {
                        addOnSuccessListener {
                            Log.d(GEOFENCE_REMOVE_WORK_TAG, "Geofence removed successfully!")
                            cont.resume(Unit)
                        }
                        addOnFailureListener { exception ->
                            val errorMessage = when (exception) {
                                is ApiException -> {
                                    "Error code: ${exception.statusCode}"
                                }
                                else -> exception.message ?: "Unknown error"
                            }
                            Log.e(GEOFENCE_REMOVE_WORK_TAG, "Failed to remove geofences: $errorMessage")
                            cont.resumeWithException(exception)
                        }
                    }
                }
            }
            Result.success()
        }
        catch (e: IllegalArgumentException) {
            Log.e(GEOFENCE_REMOVE_WORK_TAG, "Failed to remove geofences: ${e.message}")
            Result.failure()
        }
        catch (e: Throwable) {
            Log.e(GEOFENCE_REMOVE_WORK_TAG, "Failed to remove geofences: ${e.message}")
            Result.failure()
        }
    }
}