package com.kg.mobilecomapp.domain.repository

import okhttp3.MultipartBody
import retrofit2.Response

interface AudioRepository {
    suspend fun uploadAudio(audioFile: MultipartBody.Part): Response<Unit>

    suspend fun sendOnOrOff(isOn: Boolean): Response<Unit>

}