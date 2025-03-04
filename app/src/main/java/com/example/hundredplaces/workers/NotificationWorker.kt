package com.example.hundredplaces.workers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hundredplaces.MainActivity
import com.example.hundredplaces.R

const val NOTIFICATION_WORK_TAG = "NotificationWork"

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val placeId = inputData.getLong("PlaceId", 0L)

        return try {
            if (placeId == 0L) {
                throw IllegalArgumentException("Input data is null!")
            }
            else {
                // Make a channel if necessary
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                val name = "Nearby places"
                val description = "Send notification when user is near a place"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel("GEOFENCE_NOTIFICATION", name, importance)
                channel.description = description

                // Add the channel
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)

                //Create intent to navigate when tapping the notification
                val intent = Intent(applicationContext, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("navigateToPlacesDetails", true)
                    .putExtra("placeId", placeId)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                // Create the notification
                val builder = NotificationCompat.Builder(applicationContext, "GEOFENCE_NOTIFICATION")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(applicationContext.getString(R.string.app_name))
                    .setContentText(applicationContext.getString(R.string.one_or_more_places_nearby))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(LongArray(0))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                // Show the notification
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    NotificationManagerCompat.from(applicationContext).notify(1, builder.build())
                    Log.d(NOTIFICATION_WORK_TAG, "Notification send successfully!")
                    Result.success()
                }
                else {
                    throw Throwable("No permission granted!")
                }
            }
        }
        catch (e: IllegalArgumentException) {
            Log.e(NOTIFICATION_WORK_TAG, "${e.message}")
            Result.failure()
        }
        catch (e: Throwable) {
            Log.e(NOTIFICATION_WORK_TAG, "${e.message}")
            Result.failure()
        }
    }

}