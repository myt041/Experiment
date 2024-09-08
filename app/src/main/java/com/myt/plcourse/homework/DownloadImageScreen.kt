package com.myt.plcourse.homework

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun DownloadImagesScreen() {
    var bitmap1 by remember { mutableStateOf<Bitmap?>(null) }
    var bitmap2 by remember { mutableStateOf<Bitmap?>(null) }
    var totalTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            coroutineScope.launch {
                val job = launch(Dispatchers.IO) {
                    launch { downloadImageFromUrl(imageUrl1) }
                    launch { downloadImageFromUrl(imageUrl1) }
                    launch { downloadImageFromUrl(imageUrl1) }
                    launch { downloadImageFromUrl(imageUrl1) }
                    launch { downloadImageFromUrl(imageUrl2) }
                    launch { downloadImageFromUrl(imageUrl2) }
                    launch { downloadImageFromUrl(imageUrl2) }
                    launch { downloadImageFromUrl(imageUrl2) }
                }
                totalTime = measureTimeMillis {
                    job.join()
                }

                Log.e("totalTime","==> $totalTime")
            }
        }) {
            Text(text = "Download Images")
        }

        Spacer(modifier = Modifier.height(16.dp))

        bitmap1?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Image 1",
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        bitmap2?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Image 2",
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Total time: $totalTime ms")
    }
}

private const val imageUrl1 =
    "https://file-examples.com/storage/fef44df12666d835ba71c24/2017/10/file_example_JPG_500kB.jpg"
private const val imageUrl2 =
    "https://file-examples.com/storage/fef44df12666d835ba71c24/2017/10/file_example_PNG_500kB.png"

suspend fun downloadImageFromUrl(imageUrl: String): Bitmap? {
    return try {
        val url = URL(imageUrl)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val inputStream: InputStream = connection.inputStream
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}