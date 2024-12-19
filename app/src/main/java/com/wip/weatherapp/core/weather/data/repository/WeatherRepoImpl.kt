package com.wip.weatherapp.core.weather.data.repository

import com.wip.weatherapp.core.weather.data.Forecast
import com.wip.weatherapp.core.weather.data.ForecastDao
import com.wip.weatherapp.core.weather.data.Location
import com.wip.weatherapp.core.weather.data.LocationDao
import com.wip.weatherapp.core.weather.domain.repository.WeatherRepo
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val forecastDao: ForecastDao
) :
    WeatherRepo {
    // insert location and forecast
    override suspend fun insertLocationAndForecast(location: Location, forecasts: List<Forecast>) {
        val locationId = locationDao.insertLocation(location)

        forecasts.forEach { forecast ->
            forecastDao.upsertForecast(forecast.copy(locationId = locationId))
        }
    }

    override suspend fun insertLocation(location: Location): Long {
        return locationDao.insertLocation(location)
    }

    // fetch location
    override suspend fun fetchLocation(latitude: Double, longitude: Double): Location? {
        return locationDao.getLocation(latitude, longitude)
    }

    // update five day forecast
    override suspend fun updateFiveDayForecast(locationId: Long, newForecast: List<Forecast>) {
        val forecasts = forecastDao.getFiveDayForecastByLocation(locationId)

        forecasts.forEach { forecast ->
            forecastDao.deleteForecast(forecast)
        }

        newForecast.forEach { forecast ->
            forecastDao.upsertForecast(forecast.copy(locationId = locationId))
        }
    }

    // update current day forecast
    override suspend fun updateCurrentForecast(locationId: Long, newForecast: Forecast) {
        val forecast = forecastDao.getCurrentForecastByLocation(locationId)

        forecastDao.deleteForecast(forecast)

        forecastDao.upsertForecast(newForecast.copy(locationId = locationId))
    }

    // fetch current forecasts
    override suspend fun fetchCurrentForecast(locationId: Long): Forecast {
        return forecastDao.getCurrentForecastByLocation(locationId)
    }

    // fetch five-day forecasts
    override suspend fun fetchFiveDayForecast(locationId: Long): List<Forecast> {
        return forecastDao.getFiveDayForecastByLocation(locationId)
    }
}