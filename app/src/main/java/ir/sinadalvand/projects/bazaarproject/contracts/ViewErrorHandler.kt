/*
            * *
    *  * Created by Sina Dalvand on 12/28/20 5:14 AM
    *  * Copyright (c) 2020 . All rights reserved.
    *  * Last modified 12/28/20 5:14 AM
    *
    */

    package ir.sinadalvand.projects.bazaarproject.contracts

    import androidx.annotation.DrawableRes

    // handle any type of view
    interface ViewErrorHandler {

        fun showErrorHandler(title: String, msg: String, @DrawableRes res: Int?=null, buttonText: String? = null, func: () -> Unit = {})

    fun hideErrorHandler()
}