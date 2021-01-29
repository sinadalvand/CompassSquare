/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 7:32 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 7:32 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.Flowable
import ir.sinadalvand.projects.bazaarproject.contracts.Xpository
import ir.sinadalvand.projects.bazaarproject.contracts.XrequestStatus
import ir.sinadalvand.projects.bazaarproject.data.datasource.VenueRemoteMediator
import ir.sinadalvand.projects.bazaarproject.data.local.Valutor
import ir.sinadalvand.projects.bazaarproject.data.local.VenueDao
import ir.sinadalvand.projects.bazaarproject.data.model.ResponseWrapper
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.data.remote.FoursquareApiCallInterface
import org.koin.core.inject
import retrofit2.Response

/**
 * repository for explorer api to get near place to specific location
 * @property api FoursquareApiCallInterface
 * @property venueDao VenueDao
 * @property VENUE_PAGE_SIZE Int
 * @constructor
 */
class ExploreRepository(private val api: FoursquareApiCallInterface, private val venueDao: VenueDao) : Xpository() {

    private val valutor: Valutor by inject()

    // every request size
    private val VENUE_PAGE_SIZE = 20

    /**
     * get venue as pagination
     * @param ll String lat lng in special format {"33.486207, 48.358168"}
     * @return Flowable<PagingData<Venue>>
     */
    @OptIn(ExperimentalPagingApi::class)
    fun getVenuesForLocation(ll: String): Flowable<PagingData<Venue>> {
        val pagingSourceFactory = { venueDao.getVenues() }
        return Pager(
                config = PagingConfig(pageSize = VENUE_PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = pagingSourceFactory,
                remoteMediator = VenueRemoteMediator(venueDao) {
                    dateAndLocationValidation(api.getExplore(ll, VENUE_PAGE_SIZE, (it - 1) * VENUE_PAGE_SIZE), ll)
                },
        ).flowable
    }

    // save last success location and date in shared pref
    private fun dateAndLocationValidation(api: Flowable<Response<JsonElement>>, ll: String): Flowable<ResponseWrapper<ArrayList<Venue>>> {
        return requestNetwork(api)
                .filter {
                    if (it.requestStatus == XrequestStatus.ONLINE) {
                        valutor.saveLastLocation(ll)
                        valutor.saveLastUpdateTime()
                    }
                    return@filter true
                }
                .flatMap {
                    Flowable.just(parseJsonElement2Venue(it))
                }
    }

    // parse raw json to venue array
    private fun parseJsonElement2Venue(data: ResponseWrapper<JsonElement>): ResponseWrapper<ArrayList<Venue>> {
        val status = data.requestStatus
        val message = data.message

        val response = data.data
        val venuesArray = arrayListOf<Venue>()


        // if we decide to use direct class parse then we should make new class for every depth but not is better :)
        response?.asJsonObject?.get("response")
                ?.asJsonObject
                ?.get("groups")
                ?.asJsonArray?.first()
                ?.asJsonObject?.get("items")
                ?.asJsonArray?.let {
                    for (rawVenue in it) {
                        try {
                            val venueJson = rawVenue?.asJsonObject?.get("venue")
                            val venue = Gson().fromJson(venueJson, Venue::class.java)
                            venuesArray.add(venue)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

        return ResponseWrapper(venuesArray, status, message)
    }


}