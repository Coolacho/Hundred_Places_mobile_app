package com.example.hundredplaces.workers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hundredplaces.data.model.place.repositories.PlaceRepository
import com.example.hundredplaces.data.services.distance.DistanceService
import com.example.hundredplaces.util.GeofenceBroadcastReceiver
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val GEOFENCE_ADD_WORK_TAG = "GeofenceAddingWorker"

class GeofenceAddingWorker(
    context: Context,
    params: WorkerParameters,
    private val placeRepository: PlaceRepository,
    private val distanceService: DistanceService
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        val distances = distanceService.distances.value
            .toList()
            .sortedBy { (_, value) -> value }
            .take(20)
            .toMap()

        if (distances.isEmpty()) {
            Log.d(GEOFENCE_ADD_WORK_TAG, "Distances are empty. Retrying...")
            return Result.retry()
        }

        val places = placeRepository.getAllPlaces().first().filter { place ->
            distances.contains(place.id)
        }

        return try {
            if (applicationContext.checkSelfPermission(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                throw Throwable("Permissions not granted!")
            }
            else if (places.isEmpty()) {
                throw IllegalArgumentException("Input data is empty!")
            }
            else {
                val geofencingClient = LocationServices.getGeofencingClient(applicationContext)
                val geofenceList = mutableListOf<Geofence>()
                for (place in places) {
                    geofenceList.add(
                        Geofence.Builder()
                            .setRequestId(place.id.toString())
                            .setCircularRegion(
                                place.latitude,
                                place.longitude,
                                100f
                            )
                            .setExpirationDuration(14_400_000) //4 hours
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                            .setLoiteringDelay(60_000) //300000 (5 min) for real use case
                            .setNotificationResponsiveness(300_000)
                            .build()
                    )
                }
                val geofencingRequest = GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
                    .addGeofences(geofenceList)
                    .build()

                val geofencePendingIntent: PendingIntent by lazy {
                    val intent = Intent(applicationContext, GeofenceBroadcastReceiver::class.java)
                    PendingIntent.getBroadcast(
                        applicationContext,
                        0,
                        intent,
                        PendingIntent.FLAG_MUTABLE
                    )
                }

                suspendCoroutine<Unit> { cont ->
                    geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                        addOnSuccessListener {
                            // Geofences added
                            Log.d(GEOFENCE_ADD_WORK_TAG, "Geofences added successfully!")
                            cont.resume(Unit)
                        }
                        addOnFailureListener { exception ->
                            // Failed to add geofences
                            val errorMessage = when (exception) {
                                is ApiException -> {
                                    // Get the status code for the exception
                                    // Convert status code to human-readable message
                                    when (val statusCode = exception.statusCode) {
                                        GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "Geofence service is not available now"
                                        GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> "You have exceeded the maximum number of geofences"
                                        GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> "You have exceeded the maximum number of pending intents"
                                        else -> "Error code: $statusCode"
                                    }
                                }

                                else -> exception.message ?: "Unknown error"
                            }
                            Log.e(GEOFENCE_ADD_WORK_TAG, "Failed to add geofences: $errorMessage")
                            cont.resumeWithException(exception)
                        }
                    }
                }
            }
            Result.success()
        }
        catch (e: IllegalArgumentException) {
            Log.e(GEOFENCE_ADD_WORK_TAG, "Failed to add geofences: ${e.message}")
            Result.failure()
        }
        catch (e: Throwable) {
            Log.e(GEOFENCE_ADD_WORK_TAG, "Failed to add geofences: ${e.message}")
            Result.failure()
        }
    }

}