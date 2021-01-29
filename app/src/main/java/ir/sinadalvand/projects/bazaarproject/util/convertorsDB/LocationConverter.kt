/*
 * *
 *  * Created by Sina Dalvand on 12/23/20 12:07 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/23/20 12:07 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.util.convertorsDB

import androidx.room.TypeConverter
import com.google.gson.Gson
import ir.sinadalvand.projects.bazaarproject.data.model.Location

/**
 * converter for convert {@link Location} to string and retrieve
 */
class LocationConverter {

    @TypeConverter
    fun fromTimestamp(value: String): Location? {
        return Gson().fromJson(value, Location::class.java)
    }

    @TypeConverter
    fun dateToTimestamp(date: Location?): String? {
        return Gson().toJson(date)
    }
}