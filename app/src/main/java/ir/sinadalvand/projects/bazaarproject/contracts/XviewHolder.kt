/*
 * *
 *  * Created by Sina Dalvand on 12/27/20 1:37 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/27/20 1:37 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class XviewHolder<T>(private val view: View, private val adapter: RecyclerView.Adapter<XviewHolder<T>>? = null) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(data: T, position: Int)
}