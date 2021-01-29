/*
 * *
 *  * Created by Sina Dalvand on 12/28/20 7:06 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/28/20 7:06 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

interface onRecyclerItemClicked<T> {

    fun recyclerItemClicked(data: T?, position: Int)
}