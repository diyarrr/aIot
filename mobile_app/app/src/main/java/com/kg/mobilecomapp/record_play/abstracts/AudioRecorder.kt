package com.kg.mobilecomapp.record_play.abstracts

import java.io.File

interface AudioRecorder {
    fun start(outputFile : File)
    fun stop()
}