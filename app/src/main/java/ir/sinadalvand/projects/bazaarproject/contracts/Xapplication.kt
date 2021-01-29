/*
 * *
 *  * Created by Sina Dalvand on 10/25/20 12:17 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/25/20 12:17 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

import android.app.Application
import androidx.multidex.MultiDex
import ir.sinadalvand.projects.bazaarproject.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber

/**
 * extend and override , that's all you should do ...
 * Application class with all feature for normal project
 */
abstract class Xapplication : Application() {

    /**
     * of you override then you should call super class
     */
    override fun onCreate() {

        // init MultiDex
        MultiDex.install(this)


        // set logger depth
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())


        // koin module add
        startKoin {
            androidContext(this@Xapplication)
            modules(koinModule())
        }

        super.onCreate()
    }


    /**
     * override if you have another modules to add koin
     * @return ArrayList<Module>
     */
    open fun koinModule(): ArrayList<Module> {
        return arrayListOf()
    }

}