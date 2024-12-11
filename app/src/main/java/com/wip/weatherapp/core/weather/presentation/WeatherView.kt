package com.wip.weatherapp.core.weather.presentation

import android.util.Log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.wip.weatherapp.core.weather.data.DailyForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil3.compose.AsyncImage
import com.wip.weatherapp.core.weather.domain.findTheFiveDay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.TimeZone


@Composable
fun WeatherView(latitude: Double, longitude: Double) {
    val viewModel: WeatherViewModel = viewModel()

    LaunchedEffect(Pair(latitude, longitude)) {
        viewModel.fetchCurrentWeather(latitude, longitude)
        viewModel.fetchFiveDayForecastWeather(latitude, longitude)
    }

    val currentWeather by viewModel.currentWeather.collectAsStateWithLifecycle()
    val fiveDayForecast by viewModel.fiveDayWeather.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .systemBarsPadding(),
    ) {
        WeatherHeader()

        currentWeather?.let { forecast ->

            CurrentWeather(forecast)
        }

        Spacer(modifier = Modifier.weight(1f))

        fiveDayForecast?.let { fiveDayForecast ->
            FiveDayForecastView(fiveDayForecast)
        }
    }
}

@Composable
fun WeatherHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        TODO: if there is time
//        Button(onClick = {
//            Log.d("jon", "Cities button has been clicked")
//        }) {
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                contentDescription = "Back",
//                tint = Color.Black // You can change this to your desired color
//            )
//            Text("Favorite Cities")
//        }
        Spacer(modifier = Modifier.weight(1f))

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
}

@Composable
fun CurrentWeather(forecast: CurrentForecast) {
    Column(verticalArrangement = Arrangement.Center, // Centers content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(forecast.name)
            //        TODO: if there is time
//        Button(onClick = {
//            Log.d("jon", "favorite button has been clicked")
//        }) {
//            Icon(
//                imageVector = Icons.Outlined.Star,
//                contentDescription = "Favorite City",
////                tint = Color.Black // You can change this to your desired color
//            )
//        }
        }

        Text("${forecast.main.temp}\u00B0F")

        Text("High ${forecast.main.temp_max}°F - Low ${forecast.main.temp_min}°F")
    }
}

fun getDayOfWeek(timestamp: Long): String {
    val date = Date(timestamp * 1000) // Convert seconds to milliseconds
    val format = SimpleDateFormat("EEEE", Locale.getDefault()) // Full day name (e.g., Monday)
    format.timeZone = TimeZone.getDefault() // Set to local timezone
    return format.format(date)
}

fun getMonthAndDay(timestamp: Long): String {
    val date = Date(timestamp * 1000) // Convert seconds to milliseconds
    val format =
        SimpleDateFormat("MMM. dd", Locale.getDefault()) // Abbreviated month and day (e.g., Dec 12)
    format.timeZone = TimeZone.getDefault() // Set to local timezone
    return format.format(date)
}


@Composable
fun FiveDayForecastView(forecast: DailyForecast) {
    Column {
        findTheFiveDay(forecast.list).forEach { dayForecast ->
            Row {
                Column {
                    Text(getDayOfWeek(dayForecast.dt))
                    Text(getMonthAndDay(dayForecast.dt))
                }

                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${dayForecast.weather[0].icon}@2x.png",
                    contentDescription = dayForecast.weather[0].description
                )

                Text("${dayForecast.main.temp_max}°F / ${dayForecast.main.temp_min}°F")
            }
        }
    }


}
