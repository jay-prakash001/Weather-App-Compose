package com.example.weatherappcompose.data.repos

import android.util.Log
import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.data.api.APIInstance.api
import com.example.weatherappcompose.data.api.WeatherApi
import com.example.weatherappcompose.domain.models.forecast.ForecastData
import com.example.weatherappcompose.domain.models.weather.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepoImpl (api : WeatherApi): WeatherRepo {
    override suspend fun getWeatherInfo(city:String) : Flow<Response<WeatherData>> {
        return flow {
            val res = try {
               api.getWeather(city)
            }catch (e:Exception){
                emit(Response.Error())
                Log.d("TAG", "getWeatherInfo: ${e.message}")
                return@flow
            }
            println("TAG success $res")
            emit(Response.Success(res))
        }
    }

    override suspend fun getForecastInfo(city: String): Flow<Response<ForecastData>> {
        return flow {

            val res = try {
                api.getForecast(city)
            }catch (e:Exception){
                Log.d("TAG", "getForecastInfo: ${e.message}")
                emit(Response.Error())
                return@flow
            }
            println("TAG success $res")

            emit(Response.Success(data =res))

        }
    }


}