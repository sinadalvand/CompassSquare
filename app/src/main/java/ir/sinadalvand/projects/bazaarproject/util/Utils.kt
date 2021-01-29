/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 12:40 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 12:40 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.util

import android.location.Location
import ir.sinadalvand.projects.bazaarproject.data.model.Category
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    /**
     * return current date in Int format like { 20201210 }
     * @return String
     */
    fun getDateIntFormat(): String {
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.US)
        return formatter.format(Calendar.getInstance().time)
    }


    /**
     * serialize location
     * @param location Location
     * @return String
     */
    fun location2String(location: Location): String {
        return "${location.latitude},${location.longitude}"
    }

    /**
     * convert serialized location to Location class
     * @param location String
     * @return Location
     */
    fun string2Location(location: String): Location {
        val splinter = location.split(",").toTypedArray()
        return Location("Location").apply { latitude = splinter[0].toDouble();longitude = splinter[1].toDouble() }
    }

    /**
     * get distance between two location in meter
     * @param location1 Location first location
     * @param location2 Location second location
     * @return Int distance in meter (rad distance)
     */
    fun locationDistance(location1: Location, location2: Location): Int {
        return location1.distanceTo(location2).toInt()
    }


    /**
     * convert array to formatted address
     * @param data Location?
     * @return String?
     */
    fun getAddress(data: ir.sinadalvand.projects.bazaarproject.data.model.Location?): String? {
        return data?.formattedAddress?.joinToString(" , ", transform = {
            return@joinToString it ?: ""
        })
    }


    /**
     * convert category array to string
     * @param data ArrayList<Category?>?
     * @return String?
     */
    fun getCategory(data: ArrayList<Category?>?): String? {
        return data?.joinToString(" - ", transform = {
            return@joinToString it?.name ?: ""
        })
    }


}