/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:34 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:34 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.local

import androidx.paging.PagingSource
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import ir.sinadalvand.projects.bazaarproject.data.model.Venue


@Dao
interface VenueDao {

    /**
     * insert list of venues into database
     * @param users List<Venue?>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenues(venue: List<Venue>): Completable


    /**
     * insert venue into database
     * @param users Venue
     */
    @Update
    fun insertVenue(venue: Venue)

    /**
     * get specific venue from database
     * @param id String
     * @return Flowable<Venue?>
     */
    @Query("SELECT * from Venue WHERE id=:id ")
    fun getVenue(id: String): Flowable<Venue?>

    /**
     * select and retrieve venues from database
     * @return Flowable<List<Venue?>?>?
     */
    @Query("select * from Venue")
    fun getVenues(): PagingSource<Int, Venue>

    /**
     * get records count in venue table for pagination
     * @return Flowable<Int?>?
     */
    @Query("SELECT COUNT(*) FROM Venue")
    fun getVenuesCount(): Flowable<Int?>?

    /**
     * delete all records in venus table
     */
    @Query("DELETE FROM Venue")
    fun deleteAllVenues(): Completable

}