package com.fivesysdev.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val current: CurrentWeather,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Long,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
)


