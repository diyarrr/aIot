package com.kg.mobilecomapp.data.remote


import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AudioApiService {
    @Multipart
    @POST("upload-audio")
    suspend fun uploadAudio(
        @Part audioFile: MultipartBody.Part
    ): Response<Unit>

    // after lamb/ isOn, the value of isOn wille be placed for ex : isOn true -> lamb/true
    @POST("lamb/{isOn}")
    suspend fun sendOnOrOff(
        @Path("isOn") isOn: Boolean // The @Path annotation binds the "isOn" parameter to the {isOn} placeholder in the URL.
    ): Response<Unit> // The function sends the POST request and returns a Response<Unit>, indicating no specific body is expected in the response.

}