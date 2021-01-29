/*
 * *
 *  * Created by Sina Dalvand on 12/23/20 10:09 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/23/20 10:09 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


/**
 * use this class to get last user location every 20 sec
 *
 * we just try to make it poser usage friendly
 *
 * @property context Context
 * @property mFusedLocationClient FusedLocationProviderClient
 * @property locationRequest LocationRequest
 * @property locationCallback LocationCallback
 * @property listener LocationUpdateListener?
 * @constructor
 */
class LocationManager(private val context: Context) : LifecycleObserver {

    private val mFusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    // set interval time to 20 sec and priority to PRIORITY_BALANCED_POWER_ACCURACY for best power usage
    private var locationRequest: LocationRequest = LocationRequest.create().apply { priority = LocationRequest.PRIORITY_HIGH_ACCURACY; interval = 20 * 1000 }

    private val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    private val locationResolution = MediatorLiveData<ResolvableApiException>()

    // hold last location
    var lastLocation: Location? = null

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    lastLocation = location
                    listener?.onUpdateLocation(location)
                }
            }
        }
    }

    // last location callback
    private var listener: LocationUpdateListener? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun connectListener() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return

        LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())
                .addOnSuccessListener {
                    // gps is oke so let's go
                    setupCallback()
                }
                .addOnFailureListener {
                    //request gps turn on
                    if (it is ApiException && it.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            val resolvable = it as ResolvableApiException
                            if (locationResolution.value == null)
                                locationResolution.postValue(resolvable)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }


    }

    @SuppressLint("MissingPermission")
    private fun setupCallback() {
        // get last save location on device by google service
        mFusedLocationClient.lastLocation?.addOnSuccessListener {
            it?.let {
                lastLocation = it
                listener?.onUpdateLocation(it)
            }
        }

        // start getting update interval
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun disconnectListener() {
        // remove interval and callback to save power and resource
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * set listener to get last updated location
     * @param callback LocationUpdateListener
     */
    fun setLocationUpdateListener(callback: LocationUpdateListener) {
        this.listener = callback
    }

    fun getGpsRequest(): LiveData<ResolvableApiException> {
        return locationResolution
    }

    /**
     * last location callback
     */
    interface LocationUpdateListener {

        /**
         * get user locatin in interval time period
         * @param location Location last know user location
         */
        fun onUpdateLocation(location: Location)
    }
}