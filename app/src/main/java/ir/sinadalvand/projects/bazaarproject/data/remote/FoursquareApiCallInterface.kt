/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 12:12 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 12:12 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.remote

import com.google.gson.JsonElement
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApiCallInterface {

    /**
     * get near defined location to suggested radius
     * @param ll String lat&lng in special format{"33.486207, 48.358168"}
     * @param pageSize Int
     * @param offset Int
     * @return Flowable<Response<JsonElement>>
     */
    @GET("explore?sortByPopularity=1")
    fun getExplore(@Query("ll") ll: String, @Query("limit") pageSize: Int, @Query("offset") offset: Int = 0): Flowable<Response<JsonElement>>


    /**
     * get details of special venue by getting ID
     * @param venue_id String
     * @return Flowable<JsonElement>
     */
    @GET("{venue}")
    fun getVenue(@Path("venue") venue_id: String): Flowable<Response<JsonElement>>


}