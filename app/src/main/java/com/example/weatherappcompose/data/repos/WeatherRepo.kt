package com.example.weatherappcompose.data.repos

import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.domain.models.forecast.ForecastData
import com.example.weatherappcompose.domain.models.weather.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    suspend fun getWeatherInfo(city:String) :Flow<Response<WeatherData>>
    suspend fun getForecastInfo(city:String) :Flow<Response<ForecastData>>
}
