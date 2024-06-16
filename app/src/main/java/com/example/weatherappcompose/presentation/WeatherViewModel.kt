package com.example.weatherappcompose.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.data.repos.WeatherRepo
import com.example.weatherappcompose.ui.models.forecast.ForecastData
import com.example.weatherappcompose.ui.models.weather.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
     private val _cityName = MutableStateFlow("Raipur")
    val cityName = _cityName.asStateFlow()


    fun updateCity(newValue: String) {
        _cityName.value = newValue


    }



    init {
        fetchWeather()
        fetchForecastData()
    }
    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()

    private  val _forecastData = MutableStateFlow<ForecastData?>(null)
    val forecastData = _forecastData.asStateFlow()

    fun fetchWeather(city : String = cityName.value) {
        viewModelScope.launch {
            weatherRepo.getWeatherInfo(city).collect { res ->
                when (res) {

                    is Response.Success -> {
                        res.data?.let {
                            _weatherData.value = it
                        }
                    }
                    else->{
                        Log.d("WeatherViewModel", "fetchWeatherData: ${res.data.toString()}")
                    }
                }
            }
        }
    }
    fun fetchForecastData(city: String = cityName.value){
        viewModelScope.launch {
            weatherRepo.getForecastInfo(city).collect(){
                    res->
                when(res){

                    is Response.Success -> {
                        res.data?.let {
                            _forecastData.value = it
                        }
                    }
                    else->{

                    }
                }
            }
        }
    }


}