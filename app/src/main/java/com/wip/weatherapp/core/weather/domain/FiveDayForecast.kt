package com.wip.weatherapp.core.weather.domain

import com.wip.weatherapp.core.weather.data.CurrentForecast
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun findTheFiveDay(weatherData: List<CurrentForecast>): List<CurrentForecast> {
    var startIndex = 0
    for((index, weatherForecast) in weatherData.withIndex()) {
        if (!isToday(weatherForecast.dt)) {
            startIndex = index
            break
        }
    }

    val fiveDayForecast :MutableList<CurrentForecast> = mutableListOf()

    while (fiveDayForecast.size < 5 && startIndex < weatherData.size) {
        fiveDayForecast.add(weatherData[startIndex])
        startIndex += 8
    }

    return fiveDayForecast
}

fun isToday(unixTimestamp: Long): Boolean {
    val givenDate = Instant.ofEpochSecond(unixTimestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    val today = LocalDate.now(ZoneId.systemDefault())
    return givenDate == today
}