/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 5:11 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 5:11 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.util.convertorsDB.CategoryConverter
import ir.sinadalvand.projects.bazaarproject.util.convertorsDB.LocationConverter
import ir.sinadalvand.projects.bazaarproject.util.convertorsDB.PhotosConverter

@Database(entities = [Venue::class], version = 1)
@TypeConverters(LocationConverter::class, PhotosConverter::class, CategoryConverter::class)
abstract class BazaarDatabase : RoomDatabase() {

    companion object {
        // here we used volatile annotation to prevent multi thread call cache conflict
        @Volatile
        private var INSTANCE: BazaarDatabase? = null

        private val DATABASE_NAME = "bazaar_db"

        fun getInstance(context: Context): BazaarDatabase {

            // for prevent make multiple instance of database in multi thread should lock block of code wile call this function
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, BazaarDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun venueDao(): VenueDao


}