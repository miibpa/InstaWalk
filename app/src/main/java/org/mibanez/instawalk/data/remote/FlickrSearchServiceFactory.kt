package org.mibanez.instawalk.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FlickrSearchServiceFactory {

    fun makeFlickrSearchService(isDebug: Boolean): FlickrSearchService {
        val okHttpClient = makeOkHttpClient(
            makeLoggingInterceptor((isDebug)))
        return makeFlickrSearchService(okHttpClient, Gson())
    }

    private fun makeFlickrSearchService(okHttpClient: OkHttpClient, gson: Gson): FlickrSearchService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .client(okHttpClient)

            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(FlickrSearchService::class.java)
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}