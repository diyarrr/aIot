package com.kg.mobilecomapp.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.mobilecomapp.data.remote.AudioApiService
import com.kg.mobilecomapp.data.remote.LocalHttpServer
import com.kg.mobilecomapp.dependency_injection.RetrofitManager
import com.kg.mobilecomapp.domain.repository.AudioRepository
import com.kg.mobilecomapp.record_play.abstracts.AudioPlayer
import com.kg.mobilecomapp.record_play.abstracts.AudioRecorder
import com.kg.mobilecomapp.record_play.concretes.AudioPlayerImpl
import com.kg.mobilecomapp.record_play.concretes.AudioRecorderImpl
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioPlayer : AudioPlayer,
    private val audioRecorder : AudioRecorder,
    private val repository : AudioRepository,
    private val serverState: ServerState, // Inject the HTTP server
    private val retrofitManager: RetrofitManager
) : ViewModel() {
    private val _state = mutableStateOf(AudioState())
    val state : State<AudioState> = _state
    private val outputFile : File = File(context.cacheDir, "record.mp3")
    private val api = retrofitManager.getRetrofit().create(AudioApiService::class.java)
    init {
        (audioPlayer as? AudioPlayerImpl)?.onPlaybackComplete = {
            _state.value = _state.value.copy(isPlaying = false)
        }

        observeServerState()
    }
    private fun observeServerState() {
        viewModelScope.launch {
            serverState.receivedImage.collect { file ->
                file?.let {
                    _state.value = _state.value.copy(receivedImage = it)
                }
            }
        }
    }
    fun clearIpAddress() {
        viewModelScope.launch {
            // Clear the saved IP address from SharedPreferences
            SharedPreferencesIpAddress.clearSavedIpAddress(context)
            // Reset RetrofitManager to default localhost URL
            retrofitManager.updateBaseUrl("http://localhost:3000")
        }
        SharedPreferencesIpAddress.isSaved = false
    }
    fun startRecording() {
        try {
            if (outputFile.exists()) {
                outputFile.delete() // Delete any previous recording
            }
            if(!state.value.isPlaying)
            {
                audioRecorder.start(outputFile)
                _state.value = _state.value.copy(
                    isRecording = true,
                    errorMessage = null
                )
            }
            else
            {
                _state.value = _state.value.copy(
                    errorMessage = "Cannot record audio while playing"
                )
            }
        } catch (e: SecurityException) {
            _state.value = _state.value.copy(
                isRecording = false,
                errorMessage = "SecurityException: ${e.message}"
            )
        } catch (e: IllegalStateException) {
            _state.value = _state.value.copy(
                isRecording = false,
                errorMessage = "IllegalStateException: ${e.message}"
            )
        } catch (e: IOException) {
            _state.value = _state.value.copy(
                isRecording = false,
                errorMessage = "IOException: ${e.message}"
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isRecording = false,
                errorMessage = "Exception: ${e.message}"
            )
        }
    }

    fun stopRecording() {
        try {
            audioRecorder.stop()
            _state.value = _state.value.copy(
                isRecording = false,
                errorMessage = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Failed to stop recording: ${e.message}"
            )
        }
    }

    fun playAudio() {
        try {
            if (outputFile.exists() && !state.value.isRecording) {
                audioPlayer.playFile(outputFile)
                _state.value = _state.value.copy(
                    isPlaying = true,
                    errorMessage = null
                )
            }
            else if (state.value.isRecording)
            {
                _state.value = _state.value.copy(
                    errorMessage = "Cannot play audio while recording"
                )
            }
                else {
                _state.value = _state.value.copy(
                    errorMessage = "No audio file available to play"
                )
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Failed to play audio: ${e.message}"
            )
        }
    }

    fun stopAudio() {
        try {
            audioPlayer.stop()
            _state.value = _state.value.copy(
                isPlaying = false,
                errorMessage = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Failed to stop audio: ${e.message}"
            )
        }
    }
    fun uploadAudio() {
        viewModelScope.launch {
            try {
                if (outputFile.exists() && !state.value.isRecording) {
                    _state.value = _state.value.copy(
                        isUploading = true,
                        errorMessage = null
                    )
                    val requestFile = outputFile.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                    val multipartBody = MultipartBody.Part.createFormData("audioFile", outputFile.name, requestFile)
//                    val response = repository.uploadAudio(multipartBody)
                    val response = api.uploadAudio(multipartBody)
                    if (response.isSuccessful) {
                        _state.value = _state.value.copy(
                            isUploading = false,
                            errorMessage = null
                        )
                    } else {
                    Log.d("AudioViewModel", "Failed to upload code : ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                        _state.value = _state.value.copy(
                            isUploading = false,
                            errorMessage = "isUploading = false. Failed to upload audio: ${response.code()} ${response.message()} ${response.errorBody()?.string()}"
                        )
                    }
                } else {
                    _state.value = _state.value.copy(
                        errorMessage = "No audio file to upload"
                    )
                }
            } catch (e: Exception) {
                Log.d("AudioViewModel" , "Failed to upload audio: ${e.message} , ${e.cause} , ${e.stackTrace}")
                _state.value = _state.value.copy(
                    isUploading = false,
                    errorMessage = "Exception. Failed to upload audio: ${e.message} , ${e.cause} , ${e.stackTrace}"
                )
            }
        }
    }
}

//    fun startRecording(){
//        audioRecorder.start(outputFile)
//        _state.value = state.value.copy(isRecording = true)
//    }
//
//    fun stopRecording(){
//        audioRecorder.stop()
//        _state.value = state.value.copy(isRecording = false)
//    }
//
//    fun startPlaying(){
//        audioPlayer.playFile(outputFile)
//        _state.value = state.value.copy(isPlaying = true)
//    }
//
//    fun stopPlaying(){
//        audioPlayer.stop()
//        _state.value = state.value.copy(isPlaying = false)
//    }
// Starts recording audio
//fun startRecording() {
//    try {
//        audioRecorder.start(outputFile)
//        _state.value = _state.value.copy(isRecording = true, errorMessage = null)
//    } catch (e: Exception) {
//        _state.value = _state.value.copy(errorMessage = "Failed to start recording: ${e.message}")
//    }
//}
//
//    // Stops recording audio
//    fun stopRecording() {
//        try {
//            audioRecorder.stop()
//            _state.value = _state.value.copy(isRecording = false, errorMessage = null)
//        } catch (e: Exception) {
//            _state.value = _state.value.copy(errorMessage = "Failed to stop recording: ${e.message}")
//        }
//    }
//
//    // Plays the recorded audio
//    fun playAudio() {
//            try {
//                audioPlayer.playFile(it)
//                _state.value = _state.value.copy(isPlaying = true, errorMessage = null)
//            } catch (e: Exception) {
//                _state.value = _state.value.copy(errorMessage = "Failed to play audio: ${e.message}")
//            }
//        } ?: run {
//            _state.value = _state.value.copy(errorMessage = "No audio file available to play")
//        }
//    }
//
//    // Stops playing audio
//    fun stopAudio() {
//        try {
//            audioPlayer.stop()
//            _state.value = _state.value.copy(isPlaying = false, errorMessage = null)
//        } catch (e: Exception) {
//            _state.value = _state.value.copy(errorMessage = "Failed to stop audio: ${e.message}")
//        }
//    }
//    suspend fun sendAudio(){
//        repository.uploadAudio(outputFile)
//    }