package com.fivesysdev.weatherapp.api

import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.LocationInfo
import com.fivesysdev.weatherapp.model.WeatherData
import com.fivesysdev.weatherapp.utils.Constance
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("/data/3.0/onecall?")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "alerts",
        @Query("appid") appid: String = Constance.API
    ): Response<WeatherData>


    @GET("/geo/1.0/reverse?")
    suspend fun getCityName(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = Constance.API
    ): Response<List<CityInfo>>

    @GET("/geo/1.0/direct?")
    suspend fun findLocationByName(
        @Query("q") locationName: String,
        @Query("limit") limit: Int = 20,
        @Query("appid") appid: String = Constance.API
    ): Response<List<LocationInfo>>
}