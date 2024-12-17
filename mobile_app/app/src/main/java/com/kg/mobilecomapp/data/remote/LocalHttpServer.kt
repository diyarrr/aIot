package com.kg.mobilecomapp.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.kg.mobilecomapp.MainActivity
import com.kg.mobilecomapp.MobileComApp.R
import com.kg.mobilecomapp.presentation.ServerState
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.newFixedLengthResponse
import java.io.File
import java.lang.reflect.Method
import java.util.Base64
import java.util.UUID

class LocalHttpServer(
    private val context: Context,
    private val serverState: ServerState,
    port: Int = 8080
) : NanoHTTPD(port) {

    private val cacheDir = context.cacheDir

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serve(session: IHTTPSession?): Response {
        if (session?.method == Method.POST) {
            val body = mutableMapOf<String, String>()
            session.parseBody(body)

            val postData = body["postData"]
                ?: return newFixedLengthResponse(
                    Response.Status.BAD_REQUEST,
                    "text/plain",
                    "No data received"
                )

            // Save the received image
            val fileName = "intruder_image.jpg"
            val file = File(cacheDir, fileName)
            try {
                file.writeBytes(Base64.getDecoder().decode(postData))
            } catch (e: IllegalArgumentException) {
                Log.e("LocalHttpServer", "Invalid Base64 data: ${e.message} $postData")
                return newFixedLengthResponse(
                    Response.Status.BAD_REQUEST,
                    "text/plain",
                    "Invalid Base64 data"
                )
            }

            // Notify ServerState
            serverState.updateReceivedImage(file)

            // Trigger a notification for the user
            triggerNotification(file)
            serverState.clearReceivedImage()
            Log.d("LocalHttpServer", "Image received and saved!")
            return newFixedLengthResponse("Image received and saved!")
        }

        return newFixedLengthResponse(
            Response.Status.BAD_REQUEST,
            "text/plain",
            "Only POST is supported."
        )
    }
private fun triggerNotification(imageFile: File) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("image_path", imageFile.absolutePath)
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val channelId = "intruder_alert_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Intruder Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
        context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.intruder)
        .setContentTitle("Intruder Alert!")
        .setContentText("An intruder was detected. Tap to view the image.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(1, notification)
}


}


//    private fun triggerNotification(imageFile: File) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create a notification channel (required for Android 8+)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "intruder_alert_channel",
//                "Intruder Alerts",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            channel.description = "Notifications for intruder alerts"
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Build the notification
//        val notification = NotificationCompat.Builder(context, "intruder_alert_channel")
//            .setSmallIcon(R.drawable.ic_notification) // Add your app's notification icon here
//            .setContentTitle("Intruder Alert!")
//            .setContentText("An intruder was detected.")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .build()
//
//        // Show the notification
//        notificationManager.notify(1, notification)
//    }
//private fun triggerNotification(imageFile: File) {
//    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//    // Create a notification channel (required for Android 8+)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channel = NotificationChannel(
//            "intruder_alert_channel",
//            "Intruder Alerts",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        channel.description = "Notifications for intruder alerts"
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    // Create an Intent to open the app and display the intruder image
//    val intent = Intent(context, IntruderActivity::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        putExtra("image_path", imageFile.absolutePath) // Pass the image file path
//    }
//    val pendingIntent = PendingIntent.getActivity(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Ensure compatibility with newer Android versions
//    )
//
//    // Build the notification
//    val notification = NotificationCompat.Builder(context, "intruder_alert_channel")
//        .setSmallIcon(R.drawable.ic_notification) // Add your app's notification icon here
//        .setContentTitle("Intruder Alert!")
//        .setContentText("An intruder was detected. Tap to view the picture.")
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setAutoCancel(true) // Dismiss the notification when clicked
//        .setContentIntent(pendingIntent) // Set the PendingIntent
//        .build()
//
//    // Show the notification
//    notificationManager.notify(1, notification)
//}
//private fun triggerNotification(imageFile: File) {
//    val intent = Intent(context, MainActivity::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        putExtra("image_path", imageFile.absolutePath) // Pass the image file path
//    }
//    val pendingIntent = PendingIntent.getActivity(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val channelId = "intruder_alert_channel"
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channel = NotificationChannel(
//            channelId,
//            "Intruder Alerts",
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
//    }
//
//    val notification = NotificationCompat.Builder(context, channelId)
//        .setSmallIcon(R.drawable.ic_notification)
//        .setContentTitle("Intruder Alert!")
//        .setContentText("An intruder was detected. Tap to view the image.")
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setAutoCancel(true)
//        .setContentIntent(pendingIntent)
//        .build()
//
//    val notificationManager = context.getSystemService(NotificationManager::class.java)
//    notificationManager.notify(2, notification)
//}
