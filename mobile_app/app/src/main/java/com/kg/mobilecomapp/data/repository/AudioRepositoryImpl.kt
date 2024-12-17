package com.kg.mobilecomapp.data.repository

import com.kg.mobilecomapp.data.remote.AudioApiService
import com.kg.mobilecomapp.domain.repository.AudioRepository
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val apiService: AudioApiService
) : AudioRepository {
    override suspend fun uploadAudio(audioFile: MultipartBody.Part): Response<Unit> {
        return apiService.uploadAudio(audioFile)
    }

    override suspend fun sendOnOrOff(isOn: Boolean): Response<Unit> {
        return apiService.sendOnOrOff(isOn)
    }
}