package com.wip.weatherapp.searchLocation.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.wip.weatherapp.WeatherScreen
import com.wip.weatherapp.core.weather.data.GeocodedLocation
import com.wip.weatherapp.searchLocation.domain.SearchLocationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchLocationView(navHostController: NavHostController) {
    var searchLocation by remember { mutableStateOf(TextFieldValue("")) }

    val viewModel: SearchLocationViewModel = viewModel()

    val possibleLocations by viewModel.locations.collectAsStateWithLifecycle()

    var debounceJob: Job? by remember { mutableStateOf(null) }

    LaunchedEffect(searchLocation) {
        debounceJob?.cancel()

        debounceJob = launch {
            delay(300L)
            viewModel.searchLocation(searchLocation.text)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        TextField(
            value = searchLocation,
            onValueChange = { newText ->
                searchLocation = newText
            },
            placeholder = { SearchLocationPlaceholder() },
            modifier = Modifier.fillMaxWidth()

        )
        Column {
            possibleLocations.forEach { location ->
                if (location != null) {
                    Row(modifier = Modifier.padding(16.dp).clickable {
                        navHostController.navigate(WeatherScreen(
                            latitude = location.lat,
                            longitude = location.lon
                        ))
                    }) {
                        Text(location.name + ", " + location.country + ", " + location.state)
                    }

                    HorizontalDivider()
                }
            }
        }


    }

}

@Composable
fun SearchLocationPlaceholder() {
    Text("Search location")
}