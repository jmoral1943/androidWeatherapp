package com.wip.weatherapp.core.weather.data

data class CurrentForecast(val dt: Long, val main: MainWeather, val weather: List<Weather>, val name: String) {
    data class MainWeather (
        val temp: Double,
        val temp_min : Double,
        val temp_max: Double
        )
    data class Weather (
        val id: Int,
        val main: String,
        val description: String,
        val icon: String,
    )
}
