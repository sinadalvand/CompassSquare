/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:04 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:04 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.viewmodel

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.common.api.ResolvableApiException
import io.reactivex.Flowable
import ir.sinadalvand.projects.bazaarproject.contracts.XviewModel
import ir.sinadalvand.projects.bazaarproject.data.local.Valutor
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.repository.ExploreRepository
import ir.sinadalvand.projects.bazaarproject.service.LocationManager
import ir.sinadalvand.projects.bazaarproject.util.Utils
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class FragmentMainViewModel : XviewModel(), LocationManager.LocationUpdateListener {

    // inject repository
    private val exploreRepository: ExploreRepository by inject()

    // inject location manager by koin
    private val locationManager: LocationManager by inject()

    // shared pref manager
    private val valutor: Valutor by inject()


    // hold last venue page stream
    private var currentData: Flowable<PagingData<Venue>>? = null

    // get venue as paginated stream
    val pagingVenue = MediatorLiveData<PagingData<Venue>>()


    init {
        // set callback to location manager
        locationManager.setLocationUpdateListener(this)
    }


    private fun requestGetData(): LiveData<PagingData<Venue>> {
        // if last updated time is under 5 hour then true
        val timeGate = (Date().time - valutor.getLastUpdateTime()) > (5 * 3600 * 1000)


        // if new location is less than 100 meter to another one then true
        val locationGate = valutor.getLastLocation()?.let { locationManager.lastLocation?.let { second -> Utils.locationDistance(Utils.string2Location(it), second) < 100 } } ?: true


        if (currentData != null && (timeGate || locationGate)) {
            Timber.e("Refresh Rejected")
            return pagingVenue
        }

        locationManager.lastLocation?.let { loc ->
            Timber.e("Refresh Request")
            currentData = exploreRepository.getVenuesForLocation(Utils.location2String(loc))
            pagingVenue.addSource(currentData!!.toLiveData().cachedIn(viewModelScope)) { pagingVenue.postValue(it) }
        }


        return pagingVenue
    }


    // setup location manager to be lifecycle aware to return value just when we have active UI
    fun setupLocationManager(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(locationManager)
    }

    fun getGpsRequest(): LiveData<ResolvableApiException> {
        return locationManager.getGpsRequest()
    }


    override fun onUpdateLocation(location: Location) {
        Timber.e("Current Location: " + location.latitude + ":" + location.longitude)
        requestGetData()
    }


}