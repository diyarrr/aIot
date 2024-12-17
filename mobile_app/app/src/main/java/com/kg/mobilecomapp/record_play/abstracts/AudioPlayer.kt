package com.kg.mobilecomapp.record_play.abstracts

import java.io.File

interface AudioPlayer {
    fun playFile(file : File)
    fun stop()
}