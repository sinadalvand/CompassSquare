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
import com.google.gson.reflect.TypeToken
import ir.sinadalvand.projects.bazaarproject.data.model.Category
import java.lang.reflect.Type


/**
 * converter for convert {@link ir.sinadalvand.projects.bazaarproject.data.model.Category} to string and retrieve
 */
class CategoryConverter {

    @TypeConverter
    fun fromTimestamp(value: String): ArrayList<Category>? {
        val listType: Type = object : TypeToken<ArrayList<Category?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun dateToTimestamp(date: ArrayList<Category>): String? {
        return Gson().toJson(date)
    }
}