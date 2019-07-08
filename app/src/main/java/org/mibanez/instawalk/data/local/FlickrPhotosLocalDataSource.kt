package org.mibanez.instawalk.data.local

import androidx.lifecycle.LiveData

class FlickrPhotosLocalDataSource(private val flickrPhotoDatabase: FlickrPhotoDatabase) {

    fun insertFlickrPhoto(photoUrl: String) = flickrPhotoDatabase.flickrPhoroUrlDao().insertFlickrPhoto(photoUrl.toData())

    fun getFlickrPhotos(): LiveData<List<FlickrPhotoEntity>> = flickrPhotoDatabase.flickrPhoroUrlDao().getFlickrPhotos()

    fun deletePhotos() = flickrPhotoDatabase.flickrPhoroUrlDao().dropPhotos()
}

private fun String.toData(): FlickrPhotoEntity {
    return FlickrPhotoEntity(null, this)
}
