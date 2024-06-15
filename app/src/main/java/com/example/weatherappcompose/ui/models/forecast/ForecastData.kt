package com.example.weatherappcompose.ui.models.forecast

data class ForecastData(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherItem>,
    val message: Int
)