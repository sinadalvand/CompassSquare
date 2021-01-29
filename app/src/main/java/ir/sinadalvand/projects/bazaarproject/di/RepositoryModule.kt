/*
 * *
 *  * Created by Sina Dalvand on 12/25/20 4:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/25/20 4:33 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.di

import ir.sinadalvand.projects.bazaarproject.repository.ExploreRepository
import ir.sinadalvand.projects.bazaarproject.repository.VenueDetailRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory {
        ExploreRepository(get(), get())
    }

    factory {
        VenueDetailRepository(get(), get())
    }

}