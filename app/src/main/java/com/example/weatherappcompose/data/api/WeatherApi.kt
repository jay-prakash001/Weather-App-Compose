package com.example.weatherappcompose.data.api

import com.example.weatherappcompose.ui.models.forecast.ForecastData
import com.example.weatherappcompose.ui.models.weather.WeatherData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
   suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") appid:String = "43db4057e92c4d3ab74e000368db64cc"
    ): WeatherData

   @GET("forecast")
   suspend fun getForecast(@Query("q") city: String,@Query("appid") appid:String = "43db4057e92c4d3ab74e000368db64cc"): ForecastData

    companion object {
        val BASE_URL =
            "https://api.openweathermap.org/data/2.5/"
    }
}