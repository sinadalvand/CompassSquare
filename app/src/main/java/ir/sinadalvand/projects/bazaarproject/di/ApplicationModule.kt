/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:25 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:25 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.di

import ir.sinadalvand.projects.bazaarproject.data.local.Valutor
import ir.sinadalvand.projects.bazaarproject.service.LocationManager
import org.koin.dsl.module

val applicationModule = module {

    // shared preference instance
    single {
        Valutor(get())
    }


    single {
        LocationManager(get())
    }
}