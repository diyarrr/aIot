package com.kg.mobilecomapp.record_play.concretes

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.core.net.toUri
import com.kg.mobilecomapp.record_play.abstracts.AudioPlayer
import java.io.File
import javax.inject.Inject

class AudioPlayerImpl @Inject constructor(
    private val context : Context
) : AudioPlayer {

    private var player : MediaPlayer? = null

    override fun playFile(file: File) {
        player = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                stop()
                // Notify the ViewModel that playback is complete
                onPlaybackComplete?.invoke()
            }
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
    var onPlaybackComplete: (() -> Unit)? = null
}