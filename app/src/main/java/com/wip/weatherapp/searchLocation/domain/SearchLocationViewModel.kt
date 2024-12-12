package com.wip.weatherapp.searchLocation.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wip.weatherapp.BuildConfig
import com.wip.weatherapp.RetrofitInstance
import com.wip.weatherapp.core.weather.data.GeocodedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SearchLocationViewModel: ViewModel() {
    private val _locations = MutableStateFlow<List<GeocodedLocation?>>(listOf())
    val locations = _locations.asStateFlow()

    private fun updateLocations(locations: List<GeocodedLocation>) {
        _locations.update { locations }
    }

    fun searchLocation(queryLocation: String) {
       viewModelScope.launch {
           val response = try {
               RetrofitInstance.api.searchPlace(queryLocation, 5,BuildConfig.OPEN_WEATHER_MAP_API_KEY)
           } catch (e: IOException) {
               Log.e(TAG, "IOException $e")
               return@launch
           } catch (e: HttpException) {
               Log.e(TAG, "HttpException $e")
               return@launch
           }

           if(response.isSuccessful && response.body() != null) {
               updateLocations(response.body()!!)
               Log.d("jon", "${response.body()}")
           } else {
               Log.e(TAG, "Response not successful $response")
           }
       }
    }

    companion object {
        const val TAG = "SearchLocationViewModel"
    }
}