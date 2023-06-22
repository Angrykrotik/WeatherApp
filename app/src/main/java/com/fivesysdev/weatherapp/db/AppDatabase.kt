package com.fivesysdev.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fivesysdev.weatherapp.model.LocationInfoDB

@Database(
    version = 1,
    entities = [
        LocationInfoDB::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

}