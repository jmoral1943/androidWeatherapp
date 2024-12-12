package com.wip.weatherapp.core.weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(location: Location): Long

    @Query("UPDATE location SET timestamp = :timestamp WHERE id = :locationId")
    suspend fun updateLocationTimStamp(timestamp: Long, locationId: Long)

    @Query("SELECT * FROM location WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getLocation(latitude: Double, longitude: Double): Location?
}