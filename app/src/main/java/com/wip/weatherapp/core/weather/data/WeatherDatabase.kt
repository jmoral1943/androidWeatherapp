package com.wip.weatherapp.core.weather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Location::class, Forecast::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(WeatherListConverter::class) // <-- Add this line
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun forecastDao(): ForecastDao

//    companion object {
//        @Volatile
//        private var INSTANCE: WeatherDatabase? = null
//
//        fun getInstance(context: Context): WeatherDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    WeatherDatabase::class.java,
//                    "weather_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}