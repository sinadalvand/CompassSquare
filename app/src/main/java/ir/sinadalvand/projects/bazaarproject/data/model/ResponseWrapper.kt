/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 4:00 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 4:00 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.model

import ir.sinadalvand.projects.bazaarproject.contracts.XrequestStatus

/**
 * wrapper for result that receive from any data source
 * @param G result data type
 * @property data G? response data
 * @property requestStatus XrequestStatus notify every gate about current data status
 * @property message String? contain any error message
 * @constructor
 */
data class ResponseWrapper<G>(
    var data: G? = null,
    var requestStatus: XrequestStatus = XrequestStatus.LOADING,
    var message:String? = null
)