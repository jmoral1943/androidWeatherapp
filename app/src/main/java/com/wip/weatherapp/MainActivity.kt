package com.wip.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.wip.weatherapp.core.weather.presentation.WeatherView
import com.wip.weatherapp.home.presentation.HomeView
import com.wip.weatherapp.searchLocation.presentation.SearchLocationView
import com.wip.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = HomeScreen
            ) {
                composable<HomeScreen> {
                    HomeView(navController)
                }

                composable<WeatherScreen> {
                    val args = it.toRoute<WeatherScreen>()
                    WeatherView(
                        latitude = args.latitude, longitude = args.longitude,
                        navHostController = navController
                    )
                }

                composable<SearchLocationScreen> {
                    SearchLocationView(navController)
                }
            }


        }
    }
}

//            WeatherView(latitude = 40.7128, longitude = -73.9571)

@Serializable
object HomeScreen

@Serializable
object SearchLocationScreen

@Serializable
data class WeatherScreen(
    val latitude: Double,
    val longitude: Double
)