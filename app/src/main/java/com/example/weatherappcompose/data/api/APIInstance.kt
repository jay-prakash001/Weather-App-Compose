package com.example.weatherappcompose.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIInstance {
    val interseptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

    val api = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl(WeatherApi.BASE_URL)
        .client(client).build().create(WeatherApi::class.java)


}