package com.kg.mobilecomapp.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerState @Inject constructor() {

    private val _receivedImage = MutableStateFlow<File?>(null) // Holds the last received image
    val receivedImage: StateFlow<File?> = _receivedImage.asStateFlow() // Expose as immutable

    fun updateReceivedImage(file: File) {
        _receivedImage.value = file // Update the state when a new image is received
    }
    public fun clearReceivedImage() {
        _receivedImage.value = null // Clear the state when the image is handled
    }
}
