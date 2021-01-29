/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 4:20 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 4:20 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

import io.reactivex.Flowable
import ir.sinadalvand.projects.bazaarproject.data.model.ResponseWrapper
import ir.sinadalvand.projects.bazaarproject.util.Extensions.backgroundRequest
import org.koin.core.KoinComponent
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * it's a simple repository that provide 3 useful method
 * @constructor
 */
abstract class Xpository : KoinComponent {


    /* <======================= database section =======================> */
    /**
     * get query from databse and surrounding them with status
     * @param query Flowable<G>
     * @return Flowable<ResponseWrapper<G>>
     */
    protected fun <G> requestDatabase(query: Flowable<G>): Flowable<ResponseWrapper<G>> {
        return query.backgroundRequest()
                .map {
                    databaseResponseHandler(it)
                }
    }

    // surrender function for database request
    private fun <G> databaseResponseHandler(result: G): ResponseWrapper<G> {
        return ResponseWrapper<G>().apply {
            this.data = result
            this.requestStatus = XrequestStatus.OFFLINE
        }
    }


    /* <======================= network section =======================> */
    /**
     * send request async by surrounding result with status
     * @param api Flowable<Response<G>> retrofit api call
     * @return Flowable<ResponseWrapper<G>>
     */
    protected fun <G> requestNetwork(api: Flowable<Response<G>>): Flowable<ResponseWrapper<G>> {
        return api.backgroundRequest()
                .map {
                    networkResponseHandler(it)
                }.onErrorReturn { throws ->
                    errorHandler(throws)
                }
    }

    // surrounding result with status and message here
    private fun <G> networkResponseHandler(result: Response<G>): ResponseWrapper<G> {
        val code: Int = result.code()
        val response = ResponseWrapper<G>()

        // answer is ok so we should return results
        if (code in 200..299) {
            return response.apply {
                data = result.body()
                requestStatus = XrequestStatus.ONLINE
            }
        }

        return response.apply {
            requestStatus = XrequestStatus.ERROR
        }
    }

    // if any unexpected error happen this method is responsible for handle that and return what is deserve
    private fun <G> errorHandler(throwable: Throwable? = null): ResponseWrapper<G> {
        val errorType = when (throwable) {
            is ConnectException, is SocketTimeoutException, is UnknownHostException -> {
                XrequestStatus.NO_INTERNET
            }
            else -> {
                Timber.e("Network Error: ${throwable?.message}")
                throwable?.printStackTrace()
                XrequestStatus.EXCEPTION
            }
        }
        return ResponseWrapper(requestStatus = errorType)
    }

}