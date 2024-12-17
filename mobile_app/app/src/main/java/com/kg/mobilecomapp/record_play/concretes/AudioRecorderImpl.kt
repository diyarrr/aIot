package com.kg.mobilecomapp.record_play.concretes

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import com.kg.mobilecomapp.record_play.abstracts.AudioRecorder
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AudioRecorderImpl @Inject constructor(
    private val context : Context
): AudioRecorder {

    private var recorder : MediaRecorder? = null

    @RequiresApi(Build.VERSION_CODES.S)
    private fun createRecorder() : MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context) }
        else {
            MediaRecorder()
        }
    }

    override fun stop() {
        recorder?. stop()
        recorder?.reset()
        recorder = null
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            // quality of the voice
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            // QUALİTY AGAİN
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            // works for all api levels that s why we have chosen this way
            setOutputFile(FileOutputStream(outputFile).fd)
            // prepare the recorder
            prepare()
            start()
            recorder = this
        }
    }
}