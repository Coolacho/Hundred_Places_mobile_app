package com.example.hundredplaces.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.ListenableWorker
import com.example.hundredplaces.HundredPlacesApplication
import com.example.hundredplaces.MainActivity
import com.example.hundredplaces.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

const val TAG = "Geofence intent"
const val NOTIFICATION_TAG = "Notification sending"

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
                //context.hundredPlacesApplication().container.workManagerRepository.sendNotification(triggeringGeofences[0].requestId.toLong())
                sendNotification(context, triggeringGeofences[0].requestId.toLong())
                context.hundredPlacesApplication().container.workManagerRepository.removeGeofence(triggeringGeofences[0].requestId)
            }
            Log.i(TAG, "Successful transition: $geofenceTransition")
        } else {
            // Log the error.
            Log.e(TAG, "Unsuccessful transition: $geofenceTransition")
        }
    }

    fun sendNotification(context: Context, placeId: Long) {
        try {
            // Make a channel if necessary
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "Nearby places"
            val description = "Send notification when user is near a place"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val vibrationPattern = LongArray(0)
            val channel = NotificationChannel("GEOFENCE_NOTIFICATION", name, importance)
            channel.description = description
            channel.vibrationPattern = vibrationPattern

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

            //Create intent to navigate when tapping the notification
            val intent = Intent(context, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("navigateToPlacesDetails", true)
                .putExtra("placeId", placeId)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            // Create the notification
            val notification = Notification.Builder(context, "GEOFENCE_NOTIFICATION")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.place_nearby))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            // Show the notification
            if (context.checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationManager?.notify(1, notification.build())
                Log.d(NOTIFICATION_TAG, "Notification send successfully!")
            }
            else {
                throw Throwable("No permission granted!")
            }
        }
        catch (e: IllegalArgumentException) {
            Log.e(NOTIFICATION_TAG, "${e.message}")
            ListenableWorker.Result.failure()
        }
        catch (e: Throwable) {
            Log.e(NOTIFICATION_TAG, "${e.message}")
            ListenableWorker.Result.failure()
        }
    }

    fun Context.hundredPlacesApplication(): HundredPlacesApplication =
        applicationContext as HundredPlacesApplication
}