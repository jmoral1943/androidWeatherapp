package com.wip.weatherapp.core.weather.data.repository

import com.wip.weatherapp.core.weather.data.ForecastDao
import com.wip.weatherapp.core.weather.data.LocationDao
import com.wip.weatherapp.core.weather.domain.repository.WeatherRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WeatherRepoModule {
    @Provides
    fun provideWeatherRepoImpl(locationDao: LocationDao, forecastDao: ForecastDao): WeatherRepo {
        return WeatherRepoImpl(locationDao, forecastDao)
    }
}