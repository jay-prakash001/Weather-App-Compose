package com.example.weatherappcompose


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappcompose.data.api.APIInstance
import com.example.weatherappcompose.data.repos.WeatherRepoImpl
import com.example.weatherappcompose.presentation.WeatherViewModel
import com.example.weatherappcompose.ui.screens.App
import com.example.weatherappcompose.ui.theme.WeatherAppComposeTheme
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val weatherViewModel by viewModels<WeatherViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WeatherViewModel(WeatherRepoImpl(APIInstance.api)) as T
            }
        }
    }
    )

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {


//         val weatherViewModel = WeatherViewModel(WeatherRepoImpl(APIInstance.api))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

//

//                    val weatherData = weatherViewModel.weatherData.collectAsState().value
//                    val forecastData = weatherViewModel.forecastData.collectAsState().value
//
//                    Text(text = weatherData.toString() + forecastData.toString())


//                    App(modifier = Modifier.padding(innerPadding),weatherViewModel)


                    val weatherData = weatherViewModel.weatherData.collectAsState().value
                    val forecastData = weatherViewModel.forecastData.collectAsState().value
                    if (weatherData == null) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.clouds),
                                contentDescription = "cloud image",
                                modifier = Modifier.size(200.dp).padding(10.dp,20.dp)
                            )
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            )

                        }
                    } else {
                        App(
                            modifier = Modifier.padding(innerPadding),
                            weatherViewModel = weatherViewModel
                        )

                    }
                }

            }
        }

    }


}

