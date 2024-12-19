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
import com.wip.weatherapp.core.weather.data.DailyForecast
import com.wip.weatherapp.core.weather.data.Forecast
import com.wip.weatherapp.core.weather.data.Location
import com.wip.weatherapp.core.weather.data.WeatherDatabase
import com.wip.weatherapp.core.weather.data.toCurrentForecast
import com.wip.weatherapp.core.weather.data.toForecast
import com.wip.weatherapp.core.weather.domain.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepo
) :
    ViewModel() {
    private val _currentWeather = MutableStateFlow<CurrentForecast?>(null)
    val currentWeather = _currentWeather.asStateFlow()

    private val _fiveDayWeather = MutableStateFlow<DailyForecast?>(null)
    val fiveDayWeather = _fiveDayWeather.asStateFlow()


    fun fetchCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val forecast = checkDb(currentForecast = true, latitude, longitude)

            if (forecast != null) {
                _currentWeather.update { forecast[0].toCurrentForecast() }
                return@launch
            }

            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    latitude,
                    longitude,
                    "imperial",
                    BuildConfig.OPEN_WEATHER_MAP_API_KEY
                )
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
                _currentWeather.value?.let { saveCurrentForecast(latitude, longitude, it) }
            } else {
                Log.e(TAG, "Response not successful $response")
            }
        }
    }

    fun fetchFiveDayForecastWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val forecasts = checkDb(currentForecast = true, latitude, longitude)

            if (forecasts != null) {
                val fiveDayForecasts = forecasts.map { it.toCurrentForecast() }
                _fiveDayWeather.update { DailyForecast(fiveDayForecasts) }
                return@launch
            }

            val response = try {
                RetrofitInstance.api.getFiveDayForecast(
                    latitude,
                    longitude,
                    "imperial",
                    BuildConfig.OPEN_WEATHER_MAP_API_KEY
                )
            } catch (e: IOException) {
                Log.e(TAG, "IOException $e")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException $e")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                _fiveDayWeather.update { response.body() }
                Log.d("jon", "${response.body()}")
                _fiveDayWeather.value?.list?.let { saveFiveDayForecast(latitude, longitude, it) }
            } else {
                Log.e(TAG, "Response not successful $response")
            }
        }
    }


    suspend fun checkDb(
        currentForecast: Boolean,
        latitude: Double,
        longitude: Double
    ): List<Forecast>? {
        // check if the location exist
        // if not return and let http fetch
        // fetch the db entry
        val location = weatherRepository.fetchLocation(latitude, longitude) ?: return null

        // if the location.timestamp is passed current time of 10 mins then
        // expire it and let http fetch

        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000

        // Check if `oldTimestamp` occurred more than 10 minutes ago
        val isOlderThanTenMinutes = (currentTime - location.timestamp) > tenMinutesInMillis
        if (isOlderThanTenMinutes) {
            return null
        }

        // if not return currentForecast
        return if (currentForecast) {
            listOf(weatherRepository.fetchCurrentForecast(location.id))
        } else {
            weatherRepository.fetchFiveDayForecast(location.id)
        }
    }

    suspend fun saveCurrentForecast(
        latitude: Double,
        longitude: Double,
        forecast: CurrentForecast
    ) {
        val location = weatherRepository.fetchLocation(latitude, longitude)
        if (location == null) {
            val newLocation = Location(
                latitude = latitude,
                longitude = longitude,
                timestamp = System.currentTimeMillis()
            )
            weatherRepository.insertLocationAndForecast(
                newLocation,
                listOf(forecast.toForecast(true))
            )
        } else {
            weatherRepository.updateCurrentForecast(
                location.id,
                forecast.toForecast(location.id, true)
            )
        }
    }

    suspend fun saveFiveDayForecast(
        latitude: Double,
        longitude: Double,
        forecasts: List<CurrentForecast>
    ) {
        val location = weatherRepository.fetchLocation(latitude, longitude)
        if (location == null) {
            val newLocation = Location(
                latitude = latitude,
                longitude = longitude,
                timestamp = System.currentTimeMillis()
            )
            weatherRepository.insertLocationAndForecast(
                newLocation,
                forecasts.map { it.toForecast(false) })
        } else {
            weatherRepository.updateFiveDayForecast(
                location.id,
                forecasts.map { it.toForecast(location.id, false) })
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}