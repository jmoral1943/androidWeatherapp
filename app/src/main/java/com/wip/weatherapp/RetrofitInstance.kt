package com.wip.weatherapp

import com.wip.weatherapp.core.weather.data.OpenWeatherMapAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

// TODO: change this to be used in DI
object RetrofitInstance {
    val api: OpenWeatherMapAPI by lazy {
        Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(OpenWeatherMapAPI::class.java)
    }
}