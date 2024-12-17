package com.kg.mobilecomapp.dependency_injection

import com.kg.mobilecomapp.data.repository.AudioRepositoryImpl
import com.kg.mobilecomapp.domain.repository.AudioRepository
import com.kg.mobilecomapp.record_play.abstracts.AudioPlayer
import com.kg.mobilecomapp.record_play.abstracts.AudioRecorder
import com.kg.mobilecomapp.record_play.concretes.AudioPlayerImpl
import com.kg.mobilecomapp.record_play.concretes.AudioRecorderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAudioRepository(
        audioRepositoryImpl: AudioRepositoryImpl
    ): AudioRepository

    @Binds
    @Singleton
    abstract fun bindAudioPlayer(
        audioPlayerImpl: AudioPlayerImpl
    ): AudioPlayer

    @Binds
    @Singleton
    abstract fun bindAudioRecorder(
        audioRecorderImpl: AudioRecorderImpl
    ): AudioRecorder
}
