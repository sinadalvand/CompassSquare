/*
 * *
 *  * Created by Sina Dalvand on 10/25/20 10:54 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/25/20 10:54 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent

abstract class XviewModel : ViewModel(), KoinComponent {

    protected val disposables = ArrayList<Disposable>()

    override fun onCleared() {
        disposables.forEach { it.dispose() }
        super.onCleared()
    }
}