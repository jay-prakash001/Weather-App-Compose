package com.example.weatherappcompose.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherappcompose.R
import com.example.weatherappcompose.presentation.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel,
    navController: NavHostController
) {
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
        val a = weatherViewModel.weatherData.collectAsState().value
        val context = LocalContext.current

        try {
            if (a != null) {
                temp.value = a?.main?.temp?.minus(273.15).toString().substring(0, 4)
                feelsLike.value = a?.main?.feels_like?.minus(273.15).toString().substring(0, 4)
                weatherCondition.value = a?.weather?.get(0)?.main ?: "N/A"
            }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        val imgSource = "https://openweathermap.org/img/wn/${a?.weather?.get(0)?.icon}@2x.png"

        WeatherCard(temp.value, feelsLike.value, weatherCondition.value, imgSource)

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

                navController.navigate("ForecastScreen")


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
                val lat = weatherViewModel.weatherData.collectAsState().value?.coord?.lat
                val lon = weatherViewModel.weatherData.collectAsState().value?.coord?.lon
                WeatherDetailsItem(
                    "Location",
                    "${lat.toString().substring(0, 4)}, ${lon.toString().substring(0, 4)}",
                    R.drawable.location
                )
                val windSpeed: Double? =
                    weatherViewModel.weatherData.collectAsState().value?.wind?.speed
                if (windSpeed != null) {
                    WeatherDetailsItem(
                        "Wind Speed",
                        (windSpeed * 1.609).toString().substring(0, 5),
                        R.drawable.wind
                    )
                }
                val humidity =
                    weatherViewModel.weatherData.collectAsState().value?.main?.humidity
                WeatherDetailsItem("Humidity", humidity.toString(), R.drawable.humidity)

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val pressure =
                    weatherViewModel.weatherData.collectAsState().value?.main?.pressure
                WeatherDetailsItem("Pressure", pressure.toString(), R.drawable.barometer)
                val minTemp =
                    weatherViewModel.weatherData.collectAsState().value?.main?.temp_min?.minus(
                        273.15
                    )
                WeatherDetailsItem(
                    "Min Temp",
                    minTemp.toString().substring(0, 4) + "°C",
                    R.drawable.snowflake
                )
                val maxTemp =
                    weatherViewModel.weatherData.collectAsState().value?.main?.temp_max?.minus(
                        273.15
                    )
                WeatherDetailsItem(
                    "Max Temp",
                    maxTemp.toString().substring(0, 4) + "°C",
                    R.drawable.hot
                )

            }
        }

    }


}


