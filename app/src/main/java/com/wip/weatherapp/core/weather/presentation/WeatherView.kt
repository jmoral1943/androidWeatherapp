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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun WeatherView() {
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement  =  Arrangement.Center
        ) {
            Text("New York City")
            Button(onClick = {
                Log.d("jon", "favorite button has been clicked")
            }) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Favorite City",
                    tint = Color.Black // You can change this to your desired color
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement  =  Arrangement.Center
        ) {
            Text("47\u00B0F")
        }
    }
}
