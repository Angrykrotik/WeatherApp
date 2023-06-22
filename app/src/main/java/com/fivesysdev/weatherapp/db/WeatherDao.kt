package com.fivesysdev.weatherapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fivesysdev.weatherapp.model.LocationInfoDB
import com.fivesysdev.weatherapp.utils.Constance.TABLE_NAME

@Dao
abstract class WeatherDao {

    @Insert
    abstract suspend fun insert(locationInfoDB: LocationInfoDB)

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getLocationInfo(): List<LocationInfoDB>

}