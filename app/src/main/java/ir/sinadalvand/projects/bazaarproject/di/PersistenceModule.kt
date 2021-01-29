/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 9:11 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 5:11 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.di

import ir.sinadalvand.projects.bazaarproject.data.local.BazaarDatabase
import org.koin.dsl.module

/**
 * you should provide all dependency here that is related to
 * storage and save and get data from it
 */
val persistenceModule = module {

    // room database singleton instance
    single {
        BazaarDatabase.getInstance(get())
    }

    // venue Dao singleton instance
    single {
        get<BazaarDatabase>().venueDao()
    }

}