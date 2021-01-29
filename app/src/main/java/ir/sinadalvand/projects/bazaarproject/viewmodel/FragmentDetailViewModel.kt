/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:06 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:06 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.toLiveData
import ir.sinadalvand.projects.bazaarproject.contracts.XviewModel
import ir.sinadalvand.projects.bazaarproject.data.model.ResponseWrapper
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.repository.VenueDetailRepository
import org.koin.core.inject

class FragmentDetailViewModel : XviewModel() {

    private val repository:VenueDetailRepository by inject()

    val venueDetail = MediatorLiveData<ResponseWrapper<Venue?>>()

    /**
     * request venue by id
     * @param id String
     */
    fun requestVenue(id:String){
        venueDetail.addSource(repository.getVenueDetails(id).toLiveData()){
            venueDetail.postValue(it)
        }

    }

}
