package com.example.weatherappcompose.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.data.repos.WeatherRepo
import com.example.weatherappcompose.ui.models.forecast.ForecastData
import com.example.weatherappcompose.ui.models.weather.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
     val _cityName = MutableStateFlow("Raipur")
    val cityName = _cityName.asStateFlow()


    fun updateCity(newValue: String) {
        _cityName.value = newValue


    }

    init {

               fetchWeather()
               fetchForecast()

    }

    private val _weatherData = MutableStateFlow<Response<WeatherData>>(Response.Loading())
    val weatherData: StateFlow<Response<WeatherData>> = _weatherData.asStateFlow()

    private val _forecastData = MutableStateFlow<Response<ForecastData>>(Response.Loading())
    val forecastData: StateFlow<Response<ForecastData>> = _forecastData.asStateFlow()

    fun fetchWeather(city: String = cityName.value) {
        viewModelScope.launch {

            weatherRepo.getWeatherInfo(city)
                .collect { response ->
                    if (response.data != null) {
                        _weatherData.value = response
                    }
                }
        }
    }

    fun fetchForecast(city: String = cityName.value) {
        viewModelScope.launch {
            weatherRepo.getForecastInfo(city)
                .collect { response ->
                    if (response.data != null) {
                        _forecastData.value = response
                    }
                }
        }
    }

}