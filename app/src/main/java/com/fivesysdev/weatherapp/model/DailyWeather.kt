package com.fivesysdev.weatherapp.model

import com.google.gson.annotations.SerializedName

data class DailyWeather(
    @SerializedName("temp")
    val dailyTemp: DailyTemp
)
