/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 19:56 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 19:56 PM
 *
 */

@file:Suppress("UNCHECKED_CAST")

package ir.sinadalvand.projects.bazaarproject.contracts.interfaces

import ir.sinadalvand.projects.bazaarproject.contracts.XviewModel
import org.koin.core.KoinComponent
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * this class use to implement ViewModel auto binding to detext and bind view model by Classname
 * @param T : XviewModel
 * @property vm T
 */
interface ViewModelBinding<T : XviewModel> : KoinComponent {

    // ViewModel
    val vm: T

    // view model detector class
    fun getGenericEasy(): KClass<T> {
        val ops = javaClass.genericSuperclass
        val tType = (ops as ParameterizedType).actualTypeArguments[0]
        val className = tType.toString().split(" ")[1]
        return Class.forName(className).kotlin as KClass<T>
    }

}