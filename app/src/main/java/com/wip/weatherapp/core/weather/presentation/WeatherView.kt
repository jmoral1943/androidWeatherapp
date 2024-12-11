package com.wip.weatherapp.core.weather.presentation

import android.util.Log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wip.weatherapp.core.weather.domain.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wip.weatherapp.core.weather.data.CurrentForecast
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun WeatherView(latitude: Double, longitude: Double) {
    val viewModel: WeatherViewModel = viewModel()

    LaunchedEffect(Pair(latitude, longitude)) {
        viewModel.fetchCurrentWeather(latitude, longitude)
    }

    val currentWeather by viewModel.currentWeather.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .systemBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                Log.d("jon", "Cities button has been clicked")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black // You can change this to your desired color
                )
                Text("Favorite Cities")
            }

            Button(onClick = {
                Log.d("jon", "search bar has been clicked")
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for Cities",
                    tint = Color.Black // You can change this to your desired color
                )
                Text("Cities")
            }
        }

        currentWeather?.let { forecast ->

            CurrentWeather(forecast)
        }
    }
}

@Composable
fun CurrentWeather(forecast: CurrentForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(forecast.name)
        Button(onClick = {
            Log.d("jon", "favorite button has been clicked")
        }) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Favorite City",
//                tint = Color.Black // You can change this to your desired color
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("${forecast.main.temp}\u00B0F")
    }
    Text("High ${forecast.main.temp_max}°F - Low ${forecast.main.temp_min}°F")
}
