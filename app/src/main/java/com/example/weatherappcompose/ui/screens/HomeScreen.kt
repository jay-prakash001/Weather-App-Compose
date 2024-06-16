package com.example.weatherappcompose.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.data.api.APIInstance
import com.example.weatherappcompose.data.repos.WeatherRepoImpl
import com.example.weatherappcompose.presentation.WeatherViewModel
import com.example.weatherappcompose.ui.models.weather.WeatherData

@Composable
fun HomePreview(weatherViewModel: WeatherViewModel) {
    val res = weatherViewModel.weatherData.collectAsState().value

    when (res) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Error -> {
            Text("Error loading weather data")
        }
        is Response.Success -> {
            App(modifier = Modifier, weatherViewModel = weatherViewModel)
            Toast.makeText(LocalContext.current, res.data.toString(), Toast.LENGTH_SHORT).show()
        }
        else -> {
            Text("Unknown state")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier, weatherViewModel: WeatherViewModel) {
        val showSearchField = remember {
            mutableStateOf(false)
        }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showSearchField.value) {
                InputCityName(weatherViewModel = weatherViewModel) {
                    showSearchField.value = false
                }
            }
            if (!showSearchField.value) {
                Greet(showSearchField, weatherViewModel)
            }
            Spacer(modifier = Modifier.height(10.dp))
            val temp = remember {
                mutableStateOf("N/A")
            }
            val feelsLike = remember {
                mutableStateOf("N/A")
            }
            val weatherCondition = remember {
                mutableStateOf("N/A")
            }
            val a = weatherViewModel.weatherData.collectAsState().value.data
            val context = LocalContext.current
            try {
                if (a != null) {
                    temp.value = a.main.temp.minus(273.15).toString().substring(0, 5)
                    feelsLike.value = a.main.feels_like.minus(273.15).toString().substring(0, 5)
                    weatherCondition.value = a.weather[0].main
                }
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            WeatherCard(temp.value, feelsLike.value, weatherCondition.value)


//

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    //detailed current weather information screen to be implemented

                }) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                TextButton(onClick = {

                    //Lazy column to be implemented


                }) {
                    Text(text = "Next 3 Days ->")
                }
            }

            WeatherForecastCard(weatherViewModel)



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
                    val lat = weatherViewModel.weatherData.collectAsState().value.data?.coord?.lat
                    val lon = weatherViewModel.weatherData.collectAsState().value.data?.coord?.lon
                    WeatherDetailsItem(
                        "Location",
                        "${lat.toString().substring(0, 4)}, ${lon.toString().substring(0, 4)}",
                        R.drawable.location
                    )
                    val windSpeed: Double? =
                        weatherViewModel.weatherData.collectAsState().value.data?.wind?.speed
                    if (windSpeed != null) {
                        WeatherDetailsItem("Wind Speed", (windSpeed *  1.609).toString().substring(0,5), R.drawable.wind)
                    }
                    val humidity =
                        weatherViewModel.weatherData.collectAsState().value.data?.main?.humidity
                    WeatherDetailsItem("Humidity", humidity.toString(), R.drawable.humidity)

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val pressure =
                        weatherViewModel.weatherData.collectAsState().value.data?.main?.pressure
                    WeatherDetailsItem("Pressure", pressure.toString(), R.drawable.barometer)
                    val minTemp =
                        weatherViewModel.weatherData.collectAsState().value.data?.main?.temp_min?.minus(
                            273.15
                        )
                    WeatherDetailsItem(
                        "Min Temp",
                        minTemp.toString().substring(0, 4) +"°C",
                        R.drawable.snowflake
                    )
                    val maxTemp =
                        weatherViewModel.weatherData.collectAsState().value.data?.main?.temp_max?.minus(
                            273.15
                        )
                    WeatherDetailsItem("Max Temp", maxTemp.toString().substring(0, 4)+"°C", R.drawable.hot)

                }
            }

        }



}


