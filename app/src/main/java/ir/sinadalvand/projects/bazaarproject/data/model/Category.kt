/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 6:51 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 6:51 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.model

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id")
        var id: String?,
        @SerializedName("shortName")
        var name: String?
)
