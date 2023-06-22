package com.fivesysdev.weatherapp.repository

import com.fivesysdev.weatherapp.api.ApiService
import com.fivesysdev.weatherapp.db.WeatherDao
import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.LocationInfo
import com.fivesysdev.weatherapp.model.WeatherData
import com.fivesysdev.weatherapp.utils.toDB
import com.fivesysdev.weatherapp.utils.toInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: WeatherDao
) : WeatherRepository {

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
            if (response.isSuccessful) {
                response.body()?.first()
            } else {
                null
            }
        }
    }

    override suspend fun findLocationByName(cityName: String): List<LocationInfo>? {
        return apiService.findLocationByName(cityName).let { response ->
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    override suspend fun saveLocation(locationInfo: LocationInfo) = dao.insert(locationInfo.toDB())

    override suspend fun getLocationInfoList(): List<WeatherData?> = dao.getLocationInfo().map { it.toInfo() }
        .map { locationInfo ->
            getWeather(locationInfo.lat, locationInfo.lon).apply {
                this?.cityName = locationInfo.name
            }
        }


    override suspend fun haveFavourite(): Boolean = dao.getLocationInfo().isNotEmpty()


}