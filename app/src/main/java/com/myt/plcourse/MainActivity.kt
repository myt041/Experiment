package com.myt.plcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.myt.plcourse.homework.DownloadImagesScreen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DownloadImagesScreen()
        }
    }
}


@Composable
fun BirdApp() {
    var selectedBird by remember { mutableStateOf<String?>(null) }
    var sound by remember { mutableStateOf<String?>(null) }
    var delayTime by remember { mutableStateOf<Long?>(null) }

    // Function to handle bird selection
    suspend fun selectBird(birdName: String, birdSound: String, soundDelay: Long) {
        selectedBird = birdName
        sound = birdSound
        delayTime = soundDelay

//        while (true) {
//            println("$sound from ${Thread.currentThread().name}")
//
//            delay(delayTime!!)
//        }
    }

    LaunchedEffect(selectedBird) {
        if (sound != null && delayTime != null) {
            CoroutineScope(Dispatchers.Default).launch{
                while (true) {
                    println("$sound from ${Thread.currentThread().name}")
                    delay(delayTime!!)
                }
            }

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            CoroutineScope(Dispatchers.IO + CoroutineName("Coo Bird")).launch {
                selectBird("Coo Bird", "Coo", 1000L)
            }
        }) {
            Text("Coo Bird")
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO + CoroutineName("Caw Bird")).launch {
                selectBird("Caw Bird", "Caw", 2000L)
            }
        }) {
            Text("Caw Bird")
        }
        Button(onClick = {
            CoroutineScope(Dispatchers.IO + CoroutineName("Chirp Bird")).launch {
                selectBird("Chirp Bird", "Chirp", 3000L)
            }
        }) {
            Text("Chirp Bird")
        }


    }

}