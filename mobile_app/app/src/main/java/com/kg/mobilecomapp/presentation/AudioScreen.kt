package com.kg.mobilecomapp.presentation

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress
import com.kg.mobilecomapp.utility.SharedPreferencesIpAddress.clearSavedIpAddress

@Composable
fun AudioScreen (viewModel: AudioViewModel = hiltViewModel(), navController: NavController) {
    val state = viewModel.state.value
    // Layout for the screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Recording Controls
//        Text(text = "IP Address: ${SharedPreferencesIpAddress.getSavedIpAddress(LocalContext.current)}",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.inverseOnSurface)
        Button(
            onClick = {
                if (state.isRecording) {
                    viewModel.stopRecording()
                } else {
                    viewModel.startRecording()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isRecording) "Stop Recording" else "Start Recording")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Playback Controls
        Button(
            onClick = {
                if (state.isPlaying) {
                    viewModel.stopAudio()
                } else {
                    viewModel.playAudio()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isPlaying) "Stop Audio" else "Play Audio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Button
        Button(
            onClick = { viewModel.uploadAudio() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isUploading
        ) {
            Text(if (state.isUploading) "Uploading..." else "Send Audio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        state.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        Button(onClick = {
            viewModel.clearIpAddress()
            navController.navigate(Screen.IPAddressScreen.route) // Redirect to IP address input screen
        }) {
            Text("Clear IP Address")
        }
    }
}
