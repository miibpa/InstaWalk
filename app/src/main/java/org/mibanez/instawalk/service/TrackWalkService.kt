package org.mibanez.instawalk.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import org.mibanez.instawalk.R
import org.mibanez.instawalk.domain.interactor.DeletePhotos
import org.mibanez.instawalk.domain.interactor.SearchPhoto
import org.mibanez.instawalk.injection.Injector
import org.mibanez.instawalk.ui.WalkTrackActivity

class TrackWalkService : Service() {

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        // Set location updates interval to 15s to balance power consumption and tracking accuracy (took into account that an average walker needs 50-60 seconds to walk 100m)
        const val INTERVAL: Long = 15000
        const val FASTEST_INTERVAL: Long = 10000
        // Distance in m walked to request a photo
        const val DISTANCE_TRIGGER_PHOTO = 100f
    }

    private lateinit var searchPhoto: SearchPhoto
    private lateinit var deletePhotos: DeletePhotos
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val locations = mutableListOf<Location>()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        searchPhoto = Injector.provideSearchPhoto(applicationContext)
        deletePhotos = Injector.provideDeletePhotos(applicationContext)

        createNotificationChannel()
        val notificationIntent = Intent(this, WalkTrackActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("You are tracking a walk")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        deletePhotos.execute()
        stoplocationUpdates()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        checkDistanceWalked(location)
    }

    private fun checkDistanceWalked(location: Location) {
        locations += location
        if (locations.size > 1) {
            if (calculateWalkedDistanceSinceLastPhoto() >= DISTANCE_TRIGGER_PHOTO) {
                searchPhoto.execute(location.latitude, location.longitude)
                locations.clear()
            }
        }
    }

    private fun calculateWalkedDistanceSinceLastPhoto(): Float {
        var totalDistance = 0f
        locations.mapIndexed { index, location ->
            if (index <locations.size - 1) {
                totalDistance += location.distanceTo(locations[index + 1])
            }
        }
        return totalDistance
    }

    private fun startLocationUpdates() {

        // Create the location request to start receiving updates
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private fun stoplocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}