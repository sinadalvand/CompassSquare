/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 6:47 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 6:47 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Venue(
        @PrimaryKey
        var id: String,
        @SerializedName("name")
        var name: String?,
        @SerializedName("categories")
        var categories: ArrayList<Category?>?,
        @SerializedName("location")
        var location: Location?,
        @SerializedName("rating")
        var rate: Float? = 0.0f,
        @SerializedName("ratingColor")
        var rateColor: String? = "#00000000",

        /*
        * here by using exclude1 we are prevent to
        * Gson deserialize this field , but if we want use official way
        * we must use {GsonBuilder.addSerializationExclusionStrategy} to
        * prevent deserialize special field with custom annotation
        * */
        @SerializedName("exclude1")
        var photos: ArrayList<Photo?>? = arrayListOf(),

        @SerializedName("exclude2")
        var bestPhoto: String?,

        @SerializedName("exclude3")
        var like: Int? = 0,
        var nextPage: Int?,
        var prevPage: Int?,
)