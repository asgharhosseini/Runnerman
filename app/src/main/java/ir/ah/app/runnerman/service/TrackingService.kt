package ir.ah.app.runnerman.service

import android.app.*
import android.app.NotificationManager.*
import android.app.PendingIntent.*
import android.content.*
import android.os.*
import androidx.annotation.*
import androidx.core.app.*
import androidx.lifecycle.*
import ir.ah.app.runnerman.R
import ir.ah.app.runnerman.other.Constants.ACTION_PAUSE_SERVICE
import ir.ah.app.runnerman.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import ir.ah.app.runnerman.other.Constants.ACTION_START_OR_RESUME_SERVICE
import ir.ah.app.runnerman.other.Constants.ACTION_STOP_SERVICE
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_CHANNEL_ID
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_CHANNEL_NAME
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_ID
import ir.ah.app.runnerman.ui.*
import timber.log.*

class TrackingService :LifecycleService() {
    var isFirstRun = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentTitle("Running App")
                .setContentText("00:00:00")
                .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
            this, 0, Intent(
            this,
            MainActivity::class.java).also {
        it.action = ACTION_SHOW_TRACKING_FRAGMENT
    },
            FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}