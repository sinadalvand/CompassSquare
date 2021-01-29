/*
 * *
 *  * Created by Sina Dalvand on 10/25/20 10:54 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/25/20 10:54 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

import ir.sinadalvand.projects.bazaarproject.contracts.interfaces.ViewModelBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class XfragmentVm<T : XviewModel> : Xfragment(), ViewModelBinding<T> {

    override val vm: T by viewModel(this.getGenericEasy())


}

