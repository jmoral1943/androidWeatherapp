package com.wip.weatherapp.core.weather.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface ForecastDao {
    @Upsert
    suspend fun upsertForecast(forecast: Forecast)

    @Delete
    suspend fun deleteForecast(forecast: Forecast)

    @Query("SELECT * FROM forecast WHERE locationId = :locationId AND currentForecast = 0")
    suspend fun getFiveDayForecastByLocation(locationId: Long): List<Forecast>

    @Query("SELECT * FROM forecast WHERE locationId = :locationId AND currentForecast = 1")
    suspend fun getCurrentForecastByLocation(locationId: Long): Forecast
}