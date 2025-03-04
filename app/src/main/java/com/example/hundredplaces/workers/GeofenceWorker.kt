package com.example.hundredplaces.workers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hundredplaces.data.model.place.repositories.PlacesRepository
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

const val GEOFENCE_WORK_TAG = "GeofenceWorker"

class GeofenceWorker(
    context: Context,
    params: WorkerParameters,
    private val placesRepository: PlacesRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        val places = placesRepository.allPlaces.first()

        return try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
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
                            .setExpirationDuration(3600000)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                            .setLoiteringDelay(60000) //Add one 0 for 10 minutes
                            .setNotificationResponsiveness(300000)
                            .build()
                    )
                }
                val geofencingRequest = GeofencingRequest.Builder()
                    .addGeofences(geofenceList)
                    .build()

                // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
                // addGeofences() and removeGeofences().
                val geofencePendingIntent: PendingIntent by lazy {
                    val intent = Intent(applicationContext, GeofenceBroadcastReceiver::class.java)
                    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
                    // addGeofences() and removeGeofences().
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
                            Log.d(GEOFENCE_WORK_TAG, "Geofences added successfully!")
                            cont.resume(Unit)
                        }
                        addOnFailureListener {
                            // Failed to add geofences
                                exception ->
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
                            Log.e(GEOFENCE_WORK_TAG, "Failed to add geofences: $errorMessage")
                            cont.resumeWithException(exception)
                        }
                    }
                }
            }
            Result.success()
        }
        catch (e: IllegalArgumentException) {
            Log.e(GEOFENCE_WORK_TAG, "Failed to add geofences: ${e.message}")
            Result.failure()
        }
        catch (e: Throwable) {
            Log.e(GEOFENCE_WORK_TAG, "Failed to add geofences: ${e.message}")
            Result.failure()
        }
    }

}