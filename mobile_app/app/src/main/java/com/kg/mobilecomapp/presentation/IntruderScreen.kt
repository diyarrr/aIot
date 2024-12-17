package com.kg.mobilecomapp.presentation

import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun IntruderScreen(imagePath: String) {
    Log.d("IntruderScreen", "Intruder Screen e geldim. Intruder image path: $imagePath")
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Intruder Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        } else {
            Text("Failed to load the intruder image.")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { (context as? ComponentActivity)?.finish() }) {
            Text("Close")
        }
    }
}
