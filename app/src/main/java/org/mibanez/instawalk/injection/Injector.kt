package org.mibanez.instawalk.injection

import android.content.Context
import org.mibanez.instawalk.BuildConfig
import org.mibanez.instawalk.data.local.FlickrPhotoDatabase
import org.mibanez.instawalk.data.local.FlickrPhotosLocalDataSource
import org.mibanez.instawalk.data.remote.FlickrSearchDataSource
import org.mibanez.instawalk.data.remote.FlickrSearchService
import org.mibanez.instawalk.data.remote.FlickrSearchServiceFactory
import org.mibanez.instawalk.domain.interactor.DeletePhotos
import org.mibanez.instawalk.domain.interactor.GetFlickrPhotos
import org.mibanez.instawalk.domain.interactor.SearchPhoto
import org.mibanez.instawalk.domain.repository.FlickrPhotosRepository

object Injector {

    fun provideSearchPhoto(applicationContext: Context): SearchPhoto {
        return SearchPhoto(providePhotosRepository(applicationContext))
    }

    fun provideGetFlickrPhotos(applicationContext: Context): GetFlickrPhotos {
        return GetFlickrPhotos(providePhotosRepository(applicationContext))
    }

    fun provideDeletePhotos(applicationContext: Context): DeletePhotos {
        return DeletePhotos(providePhotosRepository(applicationContext))
    }

    private fun providePhotosRepository(applicationContext: Context): FlickrPhotosRepository {
        return FlickrPhotosRepository.getInstance(provideRemoteDataSource(), provideLocalDataSource(applicationContext))
    }

    private fun provideLocalDataSource(applicationContext: Context): FlickrPhotosLocalDataSource {
        return FlickrPhotosLocalDataSource(providePhotosDatabase(applicationContext))
    }

    private fun provideRemoteDataSource(): FlickrSearchDataSource {
        return FlickrSearchDataSource(provideRemoteService())
    }

    private fun provideRemoteService(): FlickrSearchService {
        return FlickrSearchServiceFactory.makeFlickrSearchService(BuildConfig.DEBUG)
    }

    private fun providePhotosDatabase(applicationContext: Context) = FlickrPhotoDatabase.getInstance(applicationContext)
}