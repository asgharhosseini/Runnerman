package ir.ah.app.runnerman.service

import android.annotation.*
import android.app.*
import android.app.NotificationManager.*
import android.app.PendingIntent.*
import android.content.*
import android.location.*
import android.os.*
import androidx.annotation.*
import androidx.core.app.*
import androidx.lifecycle.*
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.maps.model.*
import ir.ah.app.runnerman.R
import ir.ah.app.runnerman.other.*
import ir.ah.app.runnerman.other.Constants.ACTION_PAUSE_SERVICE
import ir.ah.app.runnerman.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import ir.ah.app.runnerman.other.Constants.ACTION_START_OR_RESUME_SERVICE
import ir.ah.app.runnerman.other.Constants.ACTION_STOP_SERVICE
import ir.ah.app.runnerman.other.Constants.FASTEST_LOCATION_INTERVAL
import ir.ah.app.runnerman.other.Constants.LOCATION_UPDATE_INTERVAL
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_CHANNEL_ID
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_CHANNEL_NAME
import ir.ah.app.runnerman.other.Constants.NOTIFICATION_ID
import ir.ah.app.runnerman.ui.*
import timber.log.*

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : LifecycleService() {
    var isFirstRun = true

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)

        })
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                        request,
                        locationCallback,
                        Looper.getMainLooper()
                        )
            }
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Timber.d("NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }

                }
            }
        }

        private fun addPathPoint(location: Location?) {
            location?.let {
                val position=LatLng(location.latitude,location.longitude)
                pathPoints.value?.apply {
                    last().add(position)
                    pathPoints.postValue(this)
                }
            }
        }


    }


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
        addEmptyPolyline()
        isTracking.postValue(true)
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

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    }?: pathPoints.postValue(mutableListOf(mutableListOf()))

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