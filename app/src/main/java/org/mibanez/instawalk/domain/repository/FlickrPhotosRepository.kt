package org.mibanez.instawalk.domain.repository

import androidx.lifecycle.LiveData
import org.mibanez.instawalk.data.local.FlickrPhotoEntity
import org.mibanez.instawalk.data.local.FlickrPhotosLocalDataSource
import org.mibanez.instawalk.data.remote.FlickrSearchDataSource

class FlickrPhotosRepository private constructor(
    private val flickrSearchDataSource: FlickrSearchDataSource,
    private val flickrPhotosLocalDatasource: FlickrPhotosLocalDataSource
) {

    fun searchPhoto(latitude: Double, longitude: Double) {
        val photoUrl = flickrSearchDataSource.searchPhoto(latitude, longitude)
        photoUrl.fold(ifLeft = {
        }, ifRight = {
            flickrPhotosLocalDatasource.insertFlickrPhoto(it)
        })
    }

    fun getPhotos(): LiveData<List<FlickrPhotoEntity>> = flickrPhotosLocalDatasource.getFlickrPhotos()

    fun deletePhotos() = flickrPhotosLocalDatasource.deletePhotos()

    // Singleton creation
    companion object {
        private var INSTANCE: FlickrPhotosRepository? = null
        fun getInstance(flickrSearchDataSource: FlickrSearchDataSource, flickrPhotosLocalDatasource: FlickrPhotosLocalDataSource): FlickrPhotosRepository {
            if (INSTANCE == null) {
                INSTANCE = FlickrPhotosRepository(flickrSearchDataSource, flickrPhotosLocalDatasource)
            }
            return INSTANCE as FlickrPhotosRepository
        }
    }
}