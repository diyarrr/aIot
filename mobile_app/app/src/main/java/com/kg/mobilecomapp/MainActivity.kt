package com.kg.mobilecomapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.kg.mobilecomapp.presentation.AudioScreen
import com.kg.mobilecomapp.presentation.AudioViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kg.mobilecomapp.common.IPAddress
import com.kg.mobilecomapp.common.IPAddress.IpAddress
import com.kg.mobilecomapp.dependency_injection.RetrofitManager
import com.kg.mobilecomapp.presentation.IPAddressScreen
import com.kg.mobilecomapp.presentation.IntruderScreen
import com.kg.mobilecomapp.presentation.Screen
import com.kg.mobilecomapp.presentation.ServerState
import com.kg.mobilecomapp.services.ServerForegroundService
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress.getIsSaved
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var retrofitManager: RetrofitManager

    private fun requestPermissions(REQUEST_CODE: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE
            )
        }
    }
    // Start the foreground service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start the foreground service
        val request_Code = 100
        requestPermissions(request_Code)
        val serviceIntent = Intent(this, ServerForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        Log.d("MainActivity", "Foreground service started")
        handleIntent(intent) // Handle the intent that launched the Activity
    }

    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(newIntent)
        handleIntent(newIntent) // Handle the new intent when Activity is reused
    }

private fun handleIntent(intent: Intent?) {
    setContent {
        val navController = rememberNavController()

        // Check if an image path exists in the intent
        val imagePath = intent?.getStringExtra("image_path")

        NavHost(
            navController = navController,
            startDestination = if (imagePath != null) Screen.IntruderScreen.route else if(!getIsSaved()) Screen.IPAddressScreen.route else Screen.AudioScreen.route
        ) {
            composable(route = Screen.IPAddressScreen.route) {
                IPAddressScreen(navController = navController , retrofitManager = retrofitManager)
            }
            composable(route = Screen.AudioScreen.route) {
                AudioScreen(navController = navController)
            }
            composable(route = Screen.IntruderScreen.route) {
                if (imagePath != null) {
                    IntruderScreen(imagePath = imagePath)
                } else {
                    // Optionally handle missing imagePath here
                    Log.e("Navigation", "IntruderScreen route invoked without imagePath")
                }
            }
        }

    }
}

}

        // Navigate to AudioScreen when IntruderScreen is closed
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.route == Screen.IntruderScreen.route) {
//                navController.navigate(Screen.AudioScreen.route) {
//                    popUpTo(Screen.IntruderScreen.route) { inclusive = true }
//                }
//            }
//        }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Start the foreground service
//        val intent = Intent(this, ServerForegroundService::class.java)
//        ContextCompat.startForegroundService(this, intent)
//
//        setContent {
//            val viewModel: AudioViewModel = hiltViewModel()
//
//            // Get the image path from the notification click
//            val imagePath = intent.getStringExtra("image_path")
//            Log.d("MainActivity", "Received image path: $imagePath")
//            if (imagePath != null) {
//                // If there is an image, show the IntruderScreen
//                IntruderScreen(imagePath = imagePath)
//            } else {
//                // Otherwise, show the main AudioScreen
//                AudioScreen(viewModel = viewModel)
//            }
//        }
//    }


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//            val REQUEST_CODE = 100
//            requestPermissions(100)
//        val intent = Intent(this, ServerForegroundService::class.java)
//        ContextCompat.startForegroundService(this, intent)
//
//        val imagePath = intent.getStringExtra("image_path") // Handle notification click
//        setContent {
//            val viewModel: AudioViewModel = hiltViewModel() // ViewModel setup
//
//            if (imagePath != null) {
//                // Show the IntruderScreen if the notification is clicked
//                IntruderScreen(imagePath = imagePath)
//            } else {
//                // Otherwise, show the main AudioScreen
//                AudioScreen(viewModel = viewModel)
//            }
//        }
////        setContent {
////            val viewModel: AudioViewModel = hiltViewModel() // ViewModel setup
////            val imagePath = intent.getStringExtra("image_path") // Handle notification click
////
////            // Show the AudioScreen or Intruder Screen based on the intent
////            if (imagePath != null) {
////                IntruderScreen(imagePath = imagePath)
////            } else {
////                AudioScreen(viewModel = viewModel)
////            }
////        }
//    }
//
//    private fun requestPermissions(REQUEST_CODE : Int) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
//        }
//    }
//}
//    private fun handleIntent(intent: Intent?) {
//        setContent {
//            val navController = rememberNavController()
//            NavHost(
//                navController = navController,
//                startDestination =
//                    Screen.IPAddressScreen.route
//
//            ) {
//                // IP Address Screen
//                composable(route = Screen.IPAddressScreen.route) {
//                    Log.d("MainActivity", "IP Address Screen")
//                    IPAddressScreen(navController = navController , retrofitManager = retrofitManager)
//                }
//
//                // Audio Screen
//                    val imagePath = intent?.getStringExtra("image_path")
//                if(imagePath != null){
//                    composable(route = Screen.IntruderScreen.route) {
//                        Log.d("MainActivity", "Received image path: $imagePath")
//                        IntruderScreen(imagePath = imagePath)
//                    }
//                } else {
//                composable(route = Screen.AudioScreen.route) {
//
//                        // Otherwise, show the AudioScreen
//                    AudioScreen()
//
//                }
//            }
//        }
