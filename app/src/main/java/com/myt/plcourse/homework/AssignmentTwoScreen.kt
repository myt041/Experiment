package com.myt.plcourse.homework

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.myt.plcourse.util.PhotoProcessor
import com.myt.plcourse.util.RotatingBoxScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AssignmentTwoScreen() {
    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    var backgroundColor by remember { mutableStateOf(Color.White) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        photoUri = it
    }
    
    LaunchedEffect(photoUri){
        if(photoUri != null){
            val bitmap = context.contentResolver.openInputStream(photoUri!!).use {
                BitmapFactory.decodeStream(it)
            }
            isLoading = true
            launch(Dispatchers.Default) {
                val dominantColor = PhotoProcessor.findDominantColor(bitmap)
                isLoading = false
                backgroundColor = Color(dominantColor)
            }

        }
    }
    

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                backgroundColor
            ), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RotatingBoxScreen()
        Spacer(modifier = Modifier.height(64.dp))
        Button(onClick = {
            scope.launch {
                imagePicker.launch(
                    input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }) {
            Text(text = "Pick Image")
        }

        if (isLoading) {
            Text(text = "Finding dominant color...")
        }

        if (photoUri != null) {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(context)
                    .data(data = photoUri)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null
            )
        }

    }
}