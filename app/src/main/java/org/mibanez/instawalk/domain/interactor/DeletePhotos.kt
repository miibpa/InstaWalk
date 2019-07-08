package org.mibanez.instawalk.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mibanez.instawalk.domain.repository.FlickrPhotosRepository
import kotlin.coroutines.CoroutineContext

class DeletePhotos(private val flickrPhotosRepository: FlickrPhotosRepository) : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    // Use case execution in a coroutine scope to avoid db calls in the main thread
    fun execute() = launch { flickrPhotosRepository.deletePhotos() }
}