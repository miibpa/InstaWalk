package org.mibanez.instawalk.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_walk_track.*
import org.jetbrains.anko.toast
import org.mibanez.instawalk.R
import org.mibanez.instawalk.domain.interactor.GetFlickrPhotos
import org.mibanez.instawalk.injection.Injector
import org.mibanez.instawalk.service.TrackWalkService

class WalkTrackActivity : AppCompatActivity() {

    private lateinit var getFlickrPhotos: GetFlickrPhotos
    private var flickrPhotosAdapter = PhotosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_track)
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(
            object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    checkGpsEnabled()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    toast("Must accept location permission to use this app")
                    finish()
                }
            }
        ).check()
    }

    private fun registerAdapterDataObserver() {
        flickrPhotosAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                if (flickrPhotosAdapter.itemCount == 0) textViewEmptyView.show() else textViewEmptyView.gone()
            }
        })
    }

    fun checkGpsEnabled() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        } else {
            initUI()
        }
    }

    private fun initUI() {
        buttonStart.show()
        getFlickrPhotos = Injector.provideGetFlickrPhotos(applicationContext)

        recyclerPhotos.layoutManager = LinearLayoutManager(this)
        recyclerPhotos.adapter = flickrPhotosAdapter
        registerAdapterDataObserver()
        buttonStart.setOnClickListener { startService() }
        buttonStop.setOnClickListener { stopService() }

        // Observe live data, each time a photo url is added to the database the lambda will be executed
        getFlickrPhotos.execute().observe(this, Observer {
            flickrPhotosAdapter.addPics(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    private fun startService() {
        buttonStop.show()
        buttonStart.hide()
        ContextCompat.startForegroundService(this, getServiceIntent())
    }

    private fun stopService() {
        buttonStop.hide()
        buttonStart.show()
        stopService(getServiceIntent())
    }

    private fun getServiceIntent() = Intent(this, TrackWalkService::class.java)

    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                toast("The app needs GPS to be enabled")
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11) {
            checkGpsEnabled()
        }
    }
}
