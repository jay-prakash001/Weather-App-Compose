package com.example.weatherappcompose.data.repos

import com.example.weatherappcompose.data.Response
import com.example.weatherappcompose.data.api.APIInstance.api
import com.example.weatherappcompose.data.api.WeatherApi
import com.example.weatherappcompose.ui.models.forecast.ForecastData
import com.example.weatherappcompose.ui.models.weather.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepoImpl (api : WeatherApi): WeatherRepo {
    override suspend fun getWeatherInfo(city:String) : Flow<Response<WeatherData>> {
        return flow {
            emit(Response.Loading())
            val res = try {
               api.getWeather(city)
            }catch (e:Exception){
                emit(Response.Error())
                println("REPOIMPL error ${e.message}")
                return@flow
            }
            println("REPOIMPL success ${res}")
            emit(Response.Success(res))
        }
    }

    override suspend fun getForecastInfo(city: String): Flow<Response<ForecastData>> {
        return flow {
            emit(Response.Loading())

            val res = try {
                api.getForecast(city)
            }catch (e:Exception){
                api.getForecast(city)
                emit(Response.Error())
                return@flow
            }
            emit(Response.Success(data =res))
        }
    }


}