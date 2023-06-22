package com.fivesysdev.weatherapp.repository

import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.LocationInfo
import com.fivesysdev.weatherapp.model.WeatherData

interface WeatherRepository {

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherData?

    suspend fun getCityName(latitude: Double, longitude: Double): CityInfo?

    suspend fun findLocationByName(cityName: String): List<LocationInfo>?

    suspend fun saveLocation(locationInfo: LocationInfo)

    suspend fun getLocationInfoList(): List<WeatherData?>

    suspend fun haveFavourite(): Boolean

}