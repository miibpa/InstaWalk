package org.mibanez.instawalk.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FlickrPhotoEntity::class], version = 1)
abstract class FlickrPhotoDatabase : RoomDatabase() {

    abstract fun flickrPhoroUrlDao(): FlickrPhoroUrlDao

    companion object {

        private var INSTANCE: FlickrPhotoDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): FlickrPhotoDatabase {
            if (INSTANCE == null) {
                synchronized(lock) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            FlickrPhotoDatabase::class.java, "flickrPhotos.db")
                            .build()
                    }
                    return INSTANCE as FlickrPhotoDatabase
                }
            }
            return INSTANCE as FlickrPhotoDatabase
        }
    }
}