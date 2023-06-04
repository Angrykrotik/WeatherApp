package com.fivesysdev.weatherapp.model

import com.google.gson.annotations.SerializedName

data class DailyTemp(
    @SerializedName("min")
    val minTemp: Double,
    @SerializedName("max")
    val maxTemp: Double,
    @SerializedName("pop")
    val dailyRainProbabilyty: Double
)
