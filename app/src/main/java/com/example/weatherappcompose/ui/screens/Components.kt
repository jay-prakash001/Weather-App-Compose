package com.example.weatherappcompose.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherappcompose.R
import com.example.weatherappcompose.data.api.APIInstance
import com.example.weatherappcompose.data.repos.WeatherRepoImpl
import com.example.weatherappcompose.presentation.WeatherViewModel
import com.example.weatherappcompose.ui.models.forecast.WeatherItem
import com.example.weatherappcompose.ui.models.weather.WeatherData

@Composable
fun InputCityName(modifier: Modifier = Modifier, weatherViewModel : WeatherViewModel, showSearchField: () -> Unit = {}) {
    Box(
        modifier = modifier
            .padding(0.dp, 5.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
//        weatherViewModel.cityName.collectAsState().value
        val city = remember { mutableStateOf("") }
        OutlinedTextField(singleLine = true,
            value = city.value,
            onValueChange = { city.value = it

                            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = ""
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                        weatherViewModel.fetchWeather(city.value)
                    weatherViewModel.updateCity(city.value)
                    showSearchField()

                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "")
                }
            }
        )
    }
}



@Composable
fun ForecastWeatherItem(weatherItem: WeatherItem) {

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
            text = weatherItem.dt_txt.substring(10),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        val temp = "%.2f".format((weatherItem.main.temp - 273.15))
        val feelsLike = "%.2f".format((weatherItem.main.feels_like - 273.15))

        val imgState =
        Image(painter = painterResource(id = R.drawable.clouds), contentDescription = "")
        Text(
            text = "$temp°C/$feelsLike°C",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = weatherItem.weather[0].main,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WeatherCard(temp:String = "30",feelsLike:String = "32",weatherCondition:String = "Sunny") {
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

                Text(
                    text = " $temp°C",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Feels like $feelsLike°C / $weatherCondition",
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
fun WeatherDetailsItem(name:String = "Name",value:String = "Value",icon:Int = R.drawable.clouds) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .height(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = "", modifier = Modifier.size(20.dp))
        Text(
            text = value, style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = name, style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
    }
}



@Composable
fun Greet(showSearchField: MutableState<Boolean>,weatherViewModel : WeatherViewModel ) {
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


@Composable
fun WeatherForecastCard(weatherViewModel: WeatherViewModel) {

    val weatherForecast = weatherViewModel.forecastData.collectAsState().value.data?.list
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (weatherForecast != null) {

            items(weatherForecast) {
                val a = it.weather[0].icon
                ForecastWeatherItem(it)


            }
        } else {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    LinearProgressIndicator()


                }

            }
        }

    }
}



