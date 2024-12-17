package com.kg.mobilecomapp.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kg.mobilecomapp.data.remote.LocalHttpServer
import javax.inject.Inject
import android.app.Service
import com.kg.mobilecomapp.MobileComApp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServerForegroundService : Service() {

    @Inject
    lateinit var localHttpServer: LocalHttpServer

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        startServer()
        startForeground(1, createNotification())
    }

    private fun startServer() {
        try {
            localHttpServer.start()
            Log.d("ServerForegroundService", "Server started {${localHttpServer.listeningPort}}")
            println("Server started")
        } catch (e: Exception) {
            Log.e("ServerForegroundService", "Failed to start server: ${e.message}")
            println("Failed to start server: ${e.message}")
        }
    }

    private fun createNotification(): Notification {
        val channelId = "server_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Server Service",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Server Running")
            .setContentText("The server is running in the background")
            .setSmallIcon(R.drawable.intruder) // Replace with your app's icon
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        localHttpServer.stop()
        Log.d("ServerForegroundService", "Server stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
