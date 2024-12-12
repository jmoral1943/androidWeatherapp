package com.wip.weatherapp.core.weather.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(
    tableName = "forecast",
    foreignKeys = [
        ForeignKey(
            entity = Location::class,
            parentColumns = ["id"], // Primary key in the parent entity
            childColumns = ["locationId"], // Foreign key in the child entity
            onDelete = ForeignKey.CASCADE // Action when the parent row is deleted
        )
    ],
    indices = [Index(value = ["locationId"])] // Create an index for the foreign key
)
data class Forecast(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dt: Long,
    @Embedded val main: MainWeather,
    @TypeConverters(WeatherListConverter::class) val weather: List<Weather>,
    val name: String,
    val locationId: Long,
    val currentForecast: Boolean
) {
    data class MainWeather(
        val temp: Double,
        val temp_min: Double,
        val temp_max: Double
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String,
    )
}

class WeatherListConverter {
    @TypeConverter
    fun fromWeatherList(weather: List<Forecast.Weather>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Forecast.Weather>>() {}.type
        return gson.toJson(weather, type)
    }

    @TypeConverter
    fun toWeatherList(weatherString: String): List<Forecast.Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<Forecast.Weather>>() {}.type
        return gson.fromJson(weatherString, type)
    }
}

fun Forecast.toCurrentForecast(): CurrentForecast {
    return CurrentForecast(
        dt,
        CurrentForecast.MainWeather(main.temp, main.temp_min, main.temp_max),
        weather.map { CurrentForecast.Weather(it.id, it.main, it.description, it.icon) },
        name
    )
}

fun CurrentForecast.toForecast(locationId: Long, currentForecast: Boolean): Forecast {
    return Forecast(
        dt = dt,
        main = Forecast.MainWeather(main.temp, main.temp_min, main.temp_max),
        weather = weather.map { Forecast.Weather(it.id, it.main, it.description, it.icon) },
        name = name ?: "",
        locationId = locationId,
        currentForecast = currentForecast
    )
}

fun CurrentForecast.toForecast(currentForecast: Boolean): Forecast {
    return Forecast(
        dt = dt,
        main = Forecast.MainWeather(main.temp, main.temp_min, main.temp_max),
        weather = weather.map { Forecast.Weather(it.id, it.main, it.description, it.icon) },
        name = name ?: "",
        locationId = 0,
        currentForecast = currentForecast
    )
}
