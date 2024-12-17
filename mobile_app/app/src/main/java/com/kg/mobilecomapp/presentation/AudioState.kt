package com.kg.mobilecomapp.presentation

import java.io.File

data class AudioState(
    val isRecording: Boolean = false,
    val isPlaying: Boolean = false,
    val isUploading: Boolean = false,
    val errorMessage: String? = null,
    val receivedImage: File? = null// To store the received image file
)