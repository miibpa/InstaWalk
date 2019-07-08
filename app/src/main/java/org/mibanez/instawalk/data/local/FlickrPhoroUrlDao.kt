package org.mibanez.instawalk.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlickrPhoroUrlDao {

    @Query("SELECT * FROM flickrPhotosUrl ORDER BY id DESC")
    fun getFlickrPhotos(): LiveData<List<FlickrPhotoEntity>>

    @Insert
    fun insertFlickrPhoto(flickrPhoto: FlickrPhotoEntity)

    @Query("DELETE FROM flickrPhotosUrl")
    fun dropPhotos()
}