package org.mibanez.instawalk.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mibanez.instawalk.domain.repository.FlickrPhotosRepository
import kotlin.coroutines.CoroutineContext

class SearchPhoto(private val flickrPhotosRepository: FlickrPhotosRepository) : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    // Use case execution in a coroutine scope to avoid network calls in the main thread
    fun execute(latitude: Double, longitude: Double) = launch { flickrPhotosRepository.searchPhoto(latitude, longitude) }
}