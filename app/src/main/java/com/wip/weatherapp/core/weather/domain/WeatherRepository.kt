package com.wip.weatherapp.core.weather.domain

import com.wip.weatherapp.core.weather.data.Forecast
import com.wip.weatherapp.core.weather.data.ForecastDao
import com.wip.weatherapp.core.weather.data.Location
import com.wip.weatherapp.core.weather.data.LocationDao

class WeatherRepository(
    private val locationDao: LocationDao,
    private val forecastDao: ForecastDao
) {
    // insert location and forecast
    suspend fun insertLocationAndForecast(location: Location, forecasts: List<Forecast>) {
        val locationId = locationDao.insertLocation(location)

        forecasts.forEach { forecast ->
            forecastDao.upsertForecast(forecast.copy(locationId = locationId))
        }
    }

    suspend fun insertLocation(location: Location): Long {
        return locationDao.insertLocation(location)
    }

    // fetch location
    suspend fun fetchLocation(latitude: Double, longitude: Double): Location? {
        return locationDao.getLocation(latitude, longitude)
    }

    // update five day forecast
    suspend fun updateFiveDayForecast(locationId: Long, newForecast: List<Forecast>) {
        val forecasts = forecastDao.getFiveDayForecastByLocation(locationId)

        forecasts.forEach { forecast ->
            forecastDao.deleteForecast(forecast)
        }

        newForecast.forEach { forecast ->
            forecastDao.upsertForecast(forecast.copy(locationId = locationId))
        }
    }

    // update current day forecast
    suspend fun updateCurrentForecast(locationId: Long, newForecast: Forecast) {
        val forecast = forecastDao.getCurrentForecastByLocation(locationId)

        forecastDao.deleteForecast(forecast)

        forecastDao.upsertForecast(newForecast.copy(locationId = locationId))
    }

    // fetch current forecasts
    suspend fun fetchCurrentForecast(locationId: Long): Forecast {
        return forecastDao.getCurrentForecastByLocation(locationId)
    }

    // fetch five-day forecasts
    suspend fun fetchFiveDayForecast(locationId: Long): List<Forecast> {
        return forecastDao.getFiveDayForecastByLocation(locationId)
    }
}
