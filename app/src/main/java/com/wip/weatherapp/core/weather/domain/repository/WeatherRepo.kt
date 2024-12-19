package com.wip.weatherapp.core.weather.domain.repository

import com.wip.weatherapp.core.weather.data.Forecast
import com.wip.weatherapp.core.weather.data.Location

interface WeatherRepo {
    suspend fun insertLocationAndForecast(location: Location, forecasts: List<Forecast>)

    suspend fun insertLocation(location: Location): Long

    suspend fun fetchLocation(latitude: Double, longitude: Double): Location?

    suspend fun updateFiveDayForecast(locationId: Long, newForecast: List<Forecast>)

    suspend fun updateCurrentForecast(locationId: Long, newForecast: Forecast)

    suspend fun fetchCurrentForecast(locationId: Long): Forecast

    suspend fun fetchFiveDayForecast(locationId: Long): List<Forecast>


}