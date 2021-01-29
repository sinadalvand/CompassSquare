/*
 * *
 *  * Created by Sina Dalvand on 12/27/20 10:28 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/27/20 10:28 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.di

import ir.sinadalvand.projects.bazaarproject.viewmodel.FragmentDetailViewModel
import ir.sinadalvand.projects.bazaarproject.viewmodel.FragmentMainViewModel
import org.koin.dsl.module


val viewModelModule = module {

    factory { FragmentMainViewModel() }

    factory { FragmentDetailViewModel() }

}