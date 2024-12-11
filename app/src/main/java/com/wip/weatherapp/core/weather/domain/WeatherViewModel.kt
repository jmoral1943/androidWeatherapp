package com.wip.weatherapp.core.weather.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wip.weatherapp.RetrofitInstance
import com.wip.weatherapp.core.weather.data.CurrentForecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import com.wip.weatherapp.BuildConfig

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableStateFlow<CurrentForecast?>(null)
    val currentWeather = _currentWeather.asStateFlow()


    fun fetchCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(latitude, longitude, "imperial", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
            } catch (e: IOException) {
                Log.e(TAG, "IOException $e")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException $e")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                _currentWeather.update { response.body() }
                Log.d("jon", "${response.body()}")
            } else {
                Log.e(TAG, "Response not successful $response")
            }
        }
    }

    fun fetchFiveDayForecastWeather() {

    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}