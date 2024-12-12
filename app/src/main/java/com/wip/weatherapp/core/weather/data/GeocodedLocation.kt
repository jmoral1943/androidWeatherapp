package com.wip.weatherapp.core.weather.data

data class GeocodedLocation(
    var name: String,
    var lat: Double,
    var lon: Double,
    var country: String,
    var state: String
)
