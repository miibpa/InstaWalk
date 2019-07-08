package org.mibanez.instawalk.data.remote

import com.google.gson.annotations.SerializedName

data class FlickrResponse(
    @SerializedName("stat") val status: String,
    @SerializedName("photos") val photosResponse: PhotosResponse
)

data class PhotosResponse(@SerializedName("photo") val flickrPhotos: List<FlickrPhoto>)
data class FlickrPhoto(val id: String, val farm: String, val server: String, val secret: String)