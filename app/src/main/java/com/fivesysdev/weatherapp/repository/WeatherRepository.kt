package com.fivesysdev.weatherapp.repository

import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.WeatherData

interface WeatherRepository {

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherData?

    suspend fun getCityName(latitude: Double, longitude: Double): CityInfo?

}