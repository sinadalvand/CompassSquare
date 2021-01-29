/*
 * *
 *  * Created by Sina Dalvand on 12/24/20 1:40 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/24/20 1:40 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ir.sinadalvand.projects.bazaarproject.contracts.XrequestStatus
import ir.sinadalvand.projects.bazaarproject.data.local.VenueDao
import ir.sinadalvand.projects.bazaarproject.data.model.ResponseWrapper
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.exception.NoInternetException
import ir.sinadalvand.projects.bazaarproject.util.Extensions.backgroundRequest
import java.io.InvalidObjectException


private const val STARTING_PAGE_INDEX = 1
private const val INVALID_PAGE = -1

@OptIn(ExperimentalPagingApi::class)
class VenueRemoteMediator(
        private val database: VenueDao,
        private val apiCall: (page: Int) -> Flowable<ResponseWrapper<ArrayList<Venue>>>
) : RxRemoteMediator<Int, Venue>() {


    /**
     * get next and prev page from last item saved in database
     *
     * ** unfortunately i broke COS design principle because including next and prev page in every venue record
     * ** i should make new table for page key but in small project may be ignorable
     * @param state PagingState<Int, Venue>
     * @return Pair<Int?, Int?>? first prev page , second next page
     */
    private fun getLastPageDetails(state: PagingState<Int, Venue>): Pair<Int?, Int?>? {
        return state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data?.lastOrNull()
                ?.let { venue ->
                    Pair(venue.prevPage, venue.nextPage)
                }
    }

    override fun loadSingle(loadType: LoadType, state: PagingState<Int, Venue>): Single<MediatorResult> {
        return Single.just(loadType)
                .subscribeOn(Schedulers.io())
                .map {
                    when (it) {
                        LoadType.REFRESH -> {
                            val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                            remoteKeys?.nextKey?.minus(1) ?: 1
                        }
                        LoadType.PREPEND -> {
                            val remoteKeys = getRemoteKeyForFirstItem(state) ?: throw InvalidObjectException("Result is empty")

                            remoteKeys.prevKey ?: INVALID_PAGE
                        }
                        LoadType.APPEND -> {
                            val remoteKeys = getRemoteKeyForLastItem(state) ?: throw InvalidObjectException("Result is empty")

                            remoteKeys.nextKey ?: INVALID_PAGE
                        }
                    }
                }
                .flatMap { page ->
                    if (page == INVALID_PAGE) {
                        Single.just(MediatorResult.Success(true))
                    } else {
                        apiCall.invoke(page)
                                .map<MediatorResult> {
                                    val status = it.requestStatus
                                    val data = it.data

                                    // we detect unknown exception
                                    if (status == XrequestStatus.ERROR
                                            || status == XrequestStatus.EXCEPTION)
                                        throw RuntimeException(it.message ?: "")

                                    // detect no internet
                                    if (status != XrequestStatus.ONLINE)
                                        throw NoInternetException()

                                    // doe's we reached to end of the page
                                    val endReach = status == XrequestStatus.ONLINE && data?.isEmpty() ?: false

                                    if (status == XrequestStatus.ONLINE) {
                                        insertToDb(page, loadType, data, endReach)
                                    }
                                    MediatorResult.Success(endReach)
                                }
                                .onErrorReturn {
                                    MediatorResult.Error(it)
                                }.singleOrError()
                    }

                }.onErrorReturn {
                    MediatorResult.Error(it)
                }
    }


    // let's insert new data into database
    private fun insertToDb(page: Int, loadType: LoadType, data: ArrayList<Venue>?, end: Boolean): Boolean {

        if (loadType == LoadType.REFRESH) {
            database.deleteAllVenues().backgroundRequest().backgroundRequest().blockingAwait()
        }

        // add next key to data for prepend and append state
        val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
        val nextKey = if (end) null else page + 1

        // insert new data into db
        if (data?.isNotEmpty() == true) {
            database.insertVenues(data.map { it.nextPage = nextKey; it.prevPage = prevKey; return@map it }).backgroundRequest().blockingAwait()
            return true
        }

        return false
    }

    // get next and prev page fot last item  of page
    private fun getRemoteKeyForLastItem(state: PagingState<Int, Venue>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            val data = database.getVenue(it.id).blockingFirst()
            return@let RemoteKey(data?.prevPage, data?.nextPage)
        }
    }

    // get next and prev page fot first item of page
    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Venue>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            val data = database.getVenue(it.id).blockingFirst()
            return@let RemoteKey(data?.prevPage, data?.nextPage)
        }
    }

    // get closest page key in current page
    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Venue>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                val data = database.getVenue(id).blockingFirst()
                return RemoteKey(data?.prevPage, data?.nextPage)
            }
        }
    }

    data class RemoteKey(val prevKey: Int?, val nextKey: Int?)


}