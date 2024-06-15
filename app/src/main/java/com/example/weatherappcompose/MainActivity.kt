package com.example.weatherappcompose

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.data.api.APIInstance
import com.example.weatherappcompose.data.repos.WeatherRepoImpl
import com.example.weatherappcompose.presentation.WeatherViewModel
import com.example.weatherappcompose.ui.models.weather.WeatherData
import com.example.weatherappcompose.ui.theme.WeatherAppComposeTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val weatherViewModel = WeatherViewModel(WeatherRepoImpl(APIInstance.api))

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    App(modifier = Modifier.padding(innerPadding))

                    when (weatherViewModel.weatherData.collectAsState().value) {
                        is Response.Error -> {

                            CircularProgressIndicator()
                        }

                        is Response.Loading -> {
                            LinearProgressIndicator()
                        }

                        is Response.Success -> {

                    val a = weatherViewModel.weatherData.collectAsState().value.data.toString()
                            Text(text = a)

                            val b = weatherViewModel.forecastData.collectAsState().value.data.toString()
            LazyColumn {
                item {
                    Text(text = a)

                }
                item {
                    Text(text = b)

                }
            }

                        }
                    }
                }
            }

        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun App(modifier: Modifier = Modifier) {
    val showSearchField = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSearchField.value) {
            InputCityName() {
                showSearchField.value = false
            }
        }
        if (!showSearchField.value) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Good Morning.",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "How was your day?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Light
                    )
                }
                OutlinedButton(onClick = {
                    showSearchField.value = !showSearchField.value
                    //code to show dialog to write the location to get the weather information

                }) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location")
                    Text(text = weatherViewModel.cityName.collectAsState().value)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        WeatherCard()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Next 3 Days ->")
            }
        }
        // this should be lazy row or scrollable row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ForecastWeatherItem()

        }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.inverseOnSurface,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherDetailsItem()
                WeatherDetailsItem()
                WeatherDetailsItem()

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherDetailsItem()
                WeatherDetailsItem()
                WeatherDetailsItem()

            }
        }

    }

}

@Composable
private fun WeatherDetailsItem() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "")
        Text(
            text = "30.", style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Name", style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun WeatherDetails() {

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WeatherCard() {
    Card(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.elevatedCardElevation(20.dp),
        shape = RoundedCornerShape(30.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 5.dp)
            ) {
//                    val temp = weatherViewModel.weatherInfo.collectAsState().value.data
                // Convert to Celsius

                val temp2 = String.format("%.2f", 30.26)
                Text(
                    text = " $temp2°C",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Feels like 38°C / Partially cloudy.",
                    style = MaterialTheme.typography.labelMedium,

                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }

            Image(
                painter = painterResource(id = R.drawable.clouds),
                contentDescription = "",
                modifier = Modifier
                    // Adjust to control the overflow
                    .size(100.dp, 100.dp) // Adjust size as needed
                , // Ensure the image can overflow
                contentScale = ContentScale.Fit
            )
        }

    }
}

@Composable
private fun ForecastWeatherItem() {
    Column(
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp)
            )
            .width(120.dp)
            .height(180.dp)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "9:00am",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Image(painter = painterResource(id = R.drawable.clouds), contentDescription = "")
        Text(
            text = "30/32",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Partly Cloudy",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
    }
}


@Preview(showSystemUi = true)

@Composable
fun InputCityName(modifier: Modifier = Modifier, showSearchField: () -> Unit = {}) {
    Box(
        modifier = modifier
            .padding(0.dp, 5.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {


        OutlinedTextField(singleLine = true,
            value = weatherViewModel.cityName.collectAsState().value,
            onValueChange = { weatherViewModel.updateCity(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = ""
                )
            },
            trailingIcon = {
                IconButton(onClick = {
//                        weatherViewModel.getWeatherInfo()
                    showSearchField()

                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                }
            }
        )
    }














    @Composable
    fun GetPermissions(modifier: Modifier = Modifier) {
        val permissionResultLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {


            }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
//                            permissionResultLauncher.launch(android.Manifest.permission.LOCATION_HARDWARE)
//
                permissionResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//                            permissionResultLauncher.launch(android.Manifest.permission.CAMERA)
            }) {
                Text("Allow Location")
            }

        }

    }

    suspend fun getLocations(
        fusedLocationClient: FusedLocationProviderClient,
        context: Context
    ): String {
        var location = "aa"

        // Check if permissions are granted
        val fineLocationPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationPermission && !coarseLocationPermission) {
            return "error: Location permissions not granted"
        }

        try {
            val lastLocation = fusedLocationClient.lastLocation.await()
            if (lastLocation != null) {
                location = lastLocation.time.toString()
            } else {
                location = "Location not available"
            }
        } catch (e: Exception) {
            location = "Error getting location: ${e.message}"
        }

        return location
    }
}
}

