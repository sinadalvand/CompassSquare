/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:05 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:05 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject

import ir.sinadalvand.projects.bazaarproject.contracts.Xapplication
import ir.sinadalvand.projects.bazaarproject.di.*
import org.koin.core.module.Module

class BazaarApplication : Xapplication() {


    override fun koinModule(): ArrayList<Module> {
        return arrayListOf(applicationModule, networkModule, persistenceModule, repositoryModule,viewModelModule)
    }
}