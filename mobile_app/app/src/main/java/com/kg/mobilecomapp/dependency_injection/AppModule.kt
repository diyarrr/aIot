package com.kg.mobilecomapp.dependency_injection

import android.content.Context
import com.kg.mobilecomapp.common.IPAddress
import com.kg.mobilecomapp.data.remote.AudioApiService
import com.kg.mobilecomapp.data.remote.LocalHttpServer
import com.kg.mobilecomapp.presentation.ServerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//@Provides
//@Singleton
//fun provideRetrofitManager(): RetrofitManager {
//    return RetrofitManager()
//}
@Provides
@Singleton
fun provideRetrofitManager(@ApplicationContext context: Context): RetrofitManager {
    return RetrofitManager(context)
}

    @Provides
    fun provideApiService(retrofitManager: RetrofitManager): AudioApiService {
        return retrofitManager.getRetrofit().create(AudioApiService::class.java)
    }
//    @Provides
//    @Singleton
//    fun provideAudioApiService(retrofitManager: RetrofitManager): AudioApiService {
//        return retrofitManager.getApiService()
//    }


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideServerState(): ServerState {
        return ServerState()
    }

    @Provides
    @Singleton
    fun provideLocalHttpServer(
        @ApplicationContext context: Context,
        serverState: ServerState
    ): LocalHttpServer {
        return LocalHttpServer(context, serverState)
    }
}
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .build()
//    }

//    @Provides
//    @Singleton
//    fun provideRetrofit(): AudioApiService {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build().create(AudioApiService::class.java)
//    }

    /**
     *     @Provides
     *     @Singleton
     *     fun provideStockApi() : StockApi
     *     {
     *         return Retrofit.Builder().baseUrl(StockApi.BASE_URL)
     *             .addConverterFactory(MoshiConverterFactory.create())
     *             .build()
     *             .create(StockApi::class.java)
     *     }
     */

//    private var BASE_URL = IPAddress.IpAddress
//
//
//    @Provides
//    @Singleton
//    fun provideAudioApiService(): AudioApiService
//        {
//            return Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(AudioApiService::class.java)
//        }
