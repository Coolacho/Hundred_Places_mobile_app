package com.example.hundredplaces.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.hundredplaces.HundredPlacesApplication
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

const val TAG = "Geofence intent"

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent?.geofenceTransition

        // Test that the reported transition was of interest.
        if ((geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) or
        (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)) {
            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent?.triggeringGeofences

            if (triggeringGeofences!= null) {
                // Send notification and log the transition details.
                context.hundredPlacesApplication().container.workManagerRepository.sendNotification(triggeringGeofences[0].requestId.toLong())
            }
            Log.i(TAG, "Successful transition: $geofenceTransition")
        } else {
            // Log the error.
            Log.e(TAG, "Unsuccessful transition: $geofenceTransition")
        }
    }

    fun Context.hundredPlacesApplication(): HundredPlacesApplication =
        applicationContext as HundredPlacesApplication
}