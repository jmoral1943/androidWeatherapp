package com.wip.weatherapp.home.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.wip.weatherapp.SearchLocationScreen
import com.wip.weatherapp.core.weather.presentation.WeatherView
import com.wip.weatherapp.home.domain.HomeViewModel

@Composable
fun HomeView(navHostController: NavHostController) {
    val context = LocalContext.current

    var locationIsGranted by remember { mutableStateOf(false) }

    val viewModel: HomeViewModel = viewModel()

    val currentLocationState by viewModel.currentLocation.collectAsStateWithLifecycle()

    val activity = context as? Activity

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                locationIsGranted = true
                if (activity != null) {
                    Log.d("jmorale", "activity is not null")
                    viewModel.getLocation(activity)
                }
                Log.d("jmorale", "got location")
            } else {
               locationIsGranted = false
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (activity != null) {
                Log.d("jmorale", "activity is not null")
                viewModel.getLocation(activity)
                locationIsGranted = true

            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .let {
                if (!locationIsGranted) {
                    it.blur(30.dp)
                } else {
                    it
                }
            }) {
            currentLocationState.let { coord ->
                if(coord == null) {
                    LoadingWeatherData()
                } else {
                    WeatherView(latitude = coord.latitude, longitude = coord.longitude, navHostController)
                }
            }


        }

        if (!locationIsGranted) {
            LocationIsDeniedView(navHostController)
        }
    }

}

@Composable
fun LoadingWeatherData() {
    Box(
        modifier = Modifier
            .fillMaxSize() // Fills the entire screen
            .padding(16.dp), // Optional padding
        contentAlignment = Alignment.Center // Centers content inside the Box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Aligns children to center horizontally
            verticalArrangement = Arrangement.Center // Aligns children to the center vertically
        ) {
            CircularProgressIndicator() // Loading icon
            Spacer(modifier = Modifier.height(16.dp)) // Adds spacing between the components
            Text(
                text = "Loading weather data",
                fontSize = 18.sp // Text size
            )
        }
    }
}

@Composable
fun LocationIsDeniedView(navHostController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            shape = RoundedCornerShape(16.dp),

            modifier = Modifier.fillMaxWidth(0.9f) // Make the card occupy 90% of the width
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enable Location",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "To get the current weather at your location, please enable location services.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { navHostController.navigate(SearchLocationScreen) },
                    modifier = Modifier.fillMaxWidth(0.7f) // Adjust button width
                ) {
                    Text("Search a City")
                }
            }
        }
    }
}