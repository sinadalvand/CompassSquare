/*
 * *
 *  * Created by Sina Dalvand on 12/25/20 7:35 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/25/20 7:35 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.repository

import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import ir.sinadalvand.projects.bazaarproject.contracts.Xpository
import ir.sinadalvand.projects.bazaarproject.contracts.XrequestStatus
import ir.sinadalvand.projects.bazaarproject.data.local.VenueDao
import ir.sinadalvand.projects.bazaarproject.data.model.Photo
import ir.sinadalvand.projects.bazaarproject.data.model.ResponseWrapper
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.data.remote.FoursquareApiCallInterface


class VenueDetailRepository(private val api: FoursquareApiCallInterface, private val db: VenueDao) : Xpository() {


    /**
     * get venue details by id
     * @param id String venue id
     * @return Flowable<ResponseWrapper<Venue?>>
     */
    fun getVenueDetails(id: String): Flowable<ResponseWrapper<Venue?>> {
        return Flowable.concatArrayEager(getAndSave(requestNetwork(api.getVenue(id))), requestDatabase(db.getVenue(id)))
    }

    // parse and save success data into database
    private fun getAndSave(api: Flowable<ResponseWrapper<JsonElement>>): Flowable<ResponseWrapper<Venue?>> {
        return api.flatMap {
            return@flatMap Flowable.just(getVenueObjectDetails(it))
        }.subscribeOn(Schedulers.io())
                .doOnNext {
                    if (it.requestStatus == XrequestStatus.ONLINE && it.data != null) {
                        if (it?.data?.id != null) {
                            val old = db.getVenue(it.data!!.id).blockingFirst()
                            db.insertVenue(it.data.apply {
                                this?.nextPage = old?.nextPage
                                this?.nextPage = old?.prevPage
                            }!!)
                        }
                    }
                }

    }

    // parse json element to venue by details
    private fun getVenueObjectDetails(res: ResponseWrapper<JsonElement>): ResponseWrapper<Venue?> {
        val status = res.requestStatus
        val message = res.message
        val response = res.data
        val rawVenue = response?.asJsonObject?.get("response")?.asJsonObject?.get("venue")?.asJsonObject

        val venue = Gson().fromJson(rawVenue, Venue::class.java)

        // get likes
        rawVenue?.get("likes")?.asJsonObject?.get("count")?.let { like ->
            venue.like = like.asInt
        }

        // get photos
        rawVenue?.get("photos")?.asJsonObject?.get("groups")?.asJsonArray?.first()?.asJsonObject?.get("items")?.asJsonArray?.let { photos ->
            for (photo in photos) {
                val photoUrl = photo?.asJsonObject?.get("prefix")?.asString + photo?.asJsonObject?.get("suffix")?.asString
                val user = photo?.asJsonObject?.get("user")?.asJsonObject?.get("firstName")?.asString
                venue.photos?.add(Photo(photoUrl, user))
            }
        }

        // get best photo
        rawVenue?.get("bestPhoto")?.asJsonObject?.let { photo ->
            venue.bestPhoto = photo.get("prefix")?.asString + photo.get("suffix")?.asString
        }

        return ResponseWrapper(venue, status, message)
    }


}