package com.kg.mobilecomapp.dependency_injection

import android.content.Context
import com.kg.mobilecomapp.data.remote.AudioApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
//class RetrofitManager @Inject constructor() {
//
//    private var retrofit: Retrofit = createRetrofit("http://localhost") // Placeholder URL
//
//    private fun createRetrofit(baseUrl: String): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    fun updateBaseUrl(baseUrl: String) {
//        retrofit = createRetrofit(baseUrl)
//    }
//
//    fun getApiService(): AudioApiService {
//        return retrofit.create(AudioApiService::class.java)
//    }
//}

@Singleton
class RetrofitManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var retrofit: Retrofit? = null

    // Initial default base URL (e.g., localhost)
    private var baseUrl: String = "http://localhost:3000"

    // Create Retrofit Instance
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Update Base URL
    fun updateBaseUrl(newBaseUrl: String) {
        baseUrl = newBaseUrl
        retrofit = createRetrofit()
        saveBaseUrlToPreferences(newBaseUrl)
    }

    // Get Retrofit Instance
    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            baseUrl = getBaseUrlFromPreferences() ?: baseUrl
            retrofit = createRetrofit()
        }
        return retrofit!!
    }

    private fun saveBaseUrlToPreferences(url: String) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("base_url", url).apply()
    }

    private fun getBaseUrlFromPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("base_url", null)
    }
}
