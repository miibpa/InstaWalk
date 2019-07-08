package org.mibanez.instawalk.data.remote

import org.mibanez.instawalk.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrSearchService {

    @GET("services/rest")
    fun searchPhoto(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String = BuildConfig.flickrKey,
        @Query("per_page") perPage: Int = 1,
        @Query("format") format: String = "json",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("radius") radius: Int = 1,
        @Query("nojsoncallback") noJsonCallback: Int = 1
    ): Call<FlickrResponse>
}