package com.example.weatherappcompose


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherappcompose.data.api.APIInstance
import com.example.weatherappcompose.data.repos.WeatherRepoImpl
import com.example.weatherappcompose.presentation.WeatherViewModel
import com.example.weatherappcompose.ui.screens.ForecastScreen
import com.example.weatherappcompose.ui.screens.HomeScreen
import com.example.weatherappcompose.ui.theme.WeatherAppComposeTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.plusmobileapps.konnectivity.Konnectivity

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
                val konnectivity = remember {
                    Konnectivity()
                }
                val networkState by konnectivity.currentNetworkConnectionState.collectAsState()
                val isConnected by konnectivity.isConnectedState.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isConnected) {
                        App(innerPadding)

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.network_error),
                                contentDescription = "cloud image",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(10.dp, 20.dp)
                            )
                            Text(
                                text = "Network error",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

            }
        }

    }

    @Composable
    fun App(innerPadding: PaddingValues) {
        val weatherData = weatherViewModel.weatherData.collectAsState().value
        val forecastData = weatherViewModel.forecastData.collectAsState().value
        if (weatherData == null && forecastData == null) {

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
                    modifier = Modifier
                        .size(200.dp)
                        .padding(10.dp, 20.dp)
                )
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

            }
        } else {

            Weather(modifier = Modifier.padding(innerPadding), weatherViewModel)


        }
    }


}


@Composable
fun Weather(modifier: Modifier = Modifier, weatherViewModel: WeatherViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") {
            HomeScreen(
                modifier = modifier,
                weatherViewModel = weatherViewModel,
                navController = navController
            )
        }
        composable("ForecastScreen") {
            ForecastScreen(modifier = modifier, navController, weatherViewModel)
        }
    }


}


