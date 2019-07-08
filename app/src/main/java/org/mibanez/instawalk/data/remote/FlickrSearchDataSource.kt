package org.mibanez.instawalk.data.remote

import arrow.core.Either
import arrow.core.Try
import arrow.core.left
import arrow.core.right
import org.mibanez.instawalk.domain.error.Error

class FlickrSearchDataSource(private val service: FlickrSearchService) {
    fun searchPhoto(latitude: Double, longitude: Double): Either<Error, String> {
        Try {
            service.searchPhoto(latitude = latitude, longitude = longitude).execute()
        }.fold(ifFailure = {
            return Error.ServerError.left()
        }, ifSuccess = { response ->
            return if (response.isSuccessful) {
                val body = response.body()!!
                when (body.status) {
                    "ok" -> {
                        val firstFlickrPhoto = body.photosResponse.flickrPhotos.first()
                        val flickrPhotoUrl = "https://farm${firstFlickrPhoto.farm}.staticflickr.com/${firstFlickrPhoto.server}/${firstFlickrPhoto.id}_${firstFlickrPhoto.secret}_b.jpg"
                        flickrPhotoUrl.right()
                    }
                    else -> Error.ServerError.left()
                }
            } else {
                Error.ServerError.left()
            }
        })
    }
}