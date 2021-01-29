/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 10:17 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 10:01 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

/**
 * every request must have status type
 *
 * @property XrequestStatus.LOADING every network request have this status as default
 * @property XrequestStatus.ONLINE if request came from network and be fresh then have this one
 * @property XrequestStatus.OFFLINE result that came from database contain this status
 * @property XrequestStatus.ERROR if we pass parameter or auth params wrong then we got this type of error that have message
 * @property XrequestStatus.EXCEPTION when we forget to handle some unexpected situation then we got this but always check log then get thread
 * @property XrequestStatus.NO_INTERNET this one is clear and understandable , user don't have connected internet !
 *
 */
enum class XrequestStatus {
    LOADING,ONLINE,OFFLINE, ERROR, EXCEPTION, NO_INTERNET
}