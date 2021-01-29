/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 7:32 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 7:32 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.model

data class Paging<G>(
    var data: ArrayList<G>,
    var totalPage: Int=0,
    var currentPage: Int=1,
    var totalItem: Int=0,
)