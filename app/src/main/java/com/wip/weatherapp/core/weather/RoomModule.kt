package com.wip.weatherapp.core.weather

import android.content.Context
import androidx.room.Room
import com.wip.weatherapp.core.weather.data.ForecastDao
import com.wip.weatherapp.core.weather.data.LocationDao
import com.wip.weatherapp.core.weather.data.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    fun provideLocationDao(database: WeatherDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    fun provideForecastDao(database: WeatherDatabase): ForecastDao {
        return database.forecastDao()
    }
}