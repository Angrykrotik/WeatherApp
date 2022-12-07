package com.fivesysdev.weatherapp.repository

import com.fivesysdev.weatherapp.api.ApiService
import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.WeatherData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(private val apiService: ApiService): WeatherRepository {

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherData? {
        return apiService.getWeather(latitude, longitude).let { response ->
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    override suspend fun getCityName(latitude: Double, longitude: Double): CityInfo? {
        return apiService.getCityName(latitude, longitude).let { response ->
            if (response.isSuccessful){
                response.body()?.first()
            }else{
                null
            }
        }
    }
}