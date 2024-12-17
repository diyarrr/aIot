package com.kg.mobilecomapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kg.mobilecomapp.MobileComApp
import com.kg.mobilecomapp.common.IPAddress
import com.kg.mobilecomapp.dependency_injection.RetrofitManager
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress.clearSavedIpAddress
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress.saveIpAddress

//@Composable
//fun IPAddressScreen(navController: NavController, retrofitManager: RetrofitManager
//) {
//    var ipAddress by remember { mutableStateOf("") }
//    // this will ensure that every time the IP address screen is opened, the base URL is reset to the default value
//    //retrofitManager.updateBaseUrl("http://localhost")
//    clearSavedIpAddress(LocalContext.current) // Clear the saved IP address
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(text = "Enter the IP Address of the server:", style = MaterialTheme.typography.titleLarge , color = MaterialTheme.colorScheme.inverseOnSurface)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextField(
//            value = ipAddress,
//            onValueChange = { ipAddress = it },
//            label = { Text("IP Address") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val context = LocalContext.current
//        Button(
//            onClick = {
//                val modifiedIpAdress = "http://$ipAddress:3000" // Add the port number to the IP address
//                retrofitManager.updateBaseUrl(modifiedIpAdress) // Update the base URL of the RetrofitManager
//                saveIpAddress(context, ipAddress)
//                ipAddress = "" // Clear the IP address field
//                navController.navigate(Screen.AudioScreen.route) // Navigate to the main screen
//            },
//            enabled = ipAddress.isNotBlank()
//        ) {
//            Text(text = "Continue")
//        }
//    }
//}
import androidx.compose.runtime.LaunchedEffect
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress

//@Composable
//fun IPAddressScreen(navController: NavController, retrofitManager: RetrofitManager) {
//    var ipAddress by remember { mutableStateOf("") }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Enter the IP Address of the server:",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.inverseOnSurface
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextField(
//            value = ipAddress,
//            onValueChange = { ipAddress = it },
//            label = { Text("IP Address") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val context = LocalContext.current
//        Button(
//            onClick = {
//                val modifiedIpAddress = "http://$ipAddress:3000" // Add the port number to the IP address
//                retrofitManager.updateBaseUrl(modifiedIpAddress) // Update the base URL of the RetrofitManager
//                saveIpAddress(context, modifiedIpAddress)
//                navController.navigate(Screen.AudioScreen.route) // Navigate to the main screen
//            },
//            enabled = ipAddress.isNotBlank()
//        ) {
//            Text(text = "Continue")
//        }
//    }
//}
@Composable
fun IPAddressScreen(navController: NavController, retrofitManager: RetrofitManager) {
    var ipAddress by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter Server IP Address", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text("IP Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val modifiedIpAddress = "http://$ipAddress:3000"
                retrofitManager.updateBaseUrl(modifiedIpAddress) // Update the Retrofit instance
                navController.navigate(Screen.AudioScreen.route) // Navigate to the main screen
            },
            enabled = ipAddress.isNotBlank()
        ) {
            Text("Save and Continue")
        }
    }
}
