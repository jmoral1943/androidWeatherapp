package com.wip.weatherapp.home.domain

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.wip.weatherapp.home.data.LocationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {
    private val _currentLocation  = MutableStateFlow<LocationState?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    fun updateCurrentLocation(latitude: Double, longitude: Double) {
        Log.d("jon", "updating current location")
        _currentLocation.update { LocationState(latitude, longitude) }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity) {
        val locationClient = LocationServices.getFusedLocationProviderClient(activity);

        locationClient.requestLocationUpdates(
            LocationRequest.Builder(1000).build(),
            {location -> updateCurrentLocation(location.latitude, location.longitude)},
            Looper.getMainLooper()
        ).addOnFailureListener { ex ->
            Log.e("HomeViewModel", "unable to fetch location $ex")
        }
    }
}