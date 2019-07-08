package org.mibanez.instawalk.domain.interactor

import androidx.lifecycle.LiveData
import org.mibanez.instawalk.data.local.FlickrPhotoEntity
import org.mibanez.instawalk.domain.repository.FlickrPhotosRepository

class GetFlickrPhotos(private val flickrPhotosRepository: FlickrPhotosRepository) {

    fun execute(): LiveData<List<FlickrPhotoEntity>> = flickrPhotosRepository.getPhotos()
}