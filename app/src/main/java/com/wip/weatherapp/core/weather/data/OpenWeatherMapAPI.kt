package com.wip.weatherapp.core.weather.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapAPI {
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Response<CurrentForecast>

    @GET("/data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<DailyForecast>

    @GET("/geo/1.0/direct")
    suspend fun searchPlace(
        @Query("q") location: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): Response<List<GeocodedLocation>>
}
