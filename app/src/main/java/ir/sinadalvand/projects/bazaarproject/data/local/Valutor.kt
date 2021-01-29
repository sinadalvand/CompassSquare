/*
 * *
 *  * Created by Sina Dalvand on 12/23/20 1:16 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/23/20 1:16 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.local

import android.content.Context
import ir.sinadalvand.projects.bazaarproject.contracts.Xactor
import java.util.*

/**
 * simple shared preference helper
 * @property LAST_LOCATION String
 * @property LAST_UPDATE_TIME String
 * @constructor
 */
class Valutor(context: Context) : Xactor(context, "bazaar_pref") {

    private val LAST_LOCATION = "LAST_LOCATION_KEY"
    private val LAST_UPDATE_TIME = "LAST_UPDATE_TIME_KEY"

    /**
     * return last location as string
     * @return String? location like {"33.523869,48.360973"}
     */
    fun getLastLocation(): String? {
        return get(LAST_LOCATION, null)
    }

    /**
     * save last user location that received by gps
     * @param location String save parsed location (should be like "33.523869,48.360973" )
     */
    fun saveLastLocation(location: String) {
        put(LAST_LOCATION, location)
    }


    /**
     * get last update time in timestamp
     * @return Long update time in timestamp format
     */
    fun getLastUpdateTime(): Long {
        return get(LAST_UPDATE_TIME, Date().time)
    }


    /**
     * save last update time in timestamp
     * @param time Long current time stamp
     */
    fun saveLastUpdateTime(time: Long = Date().time) {

    }

}