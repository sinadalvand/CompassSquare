/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 7:02 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 7:02 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.model

import com.google.gson.annotations.SerializedName

data class Location(
        @SerializedName("city")
        var city: String?,
        @SerializedName("state")
        var state: String?,
        @SerializedName("country")
        var country: String?,
        @SerializedName("distance")
        var distance: Int?,
        @SerializedName("formattedAddress")
        var formattedAddress: ArrayList<String?>? = null,
)
