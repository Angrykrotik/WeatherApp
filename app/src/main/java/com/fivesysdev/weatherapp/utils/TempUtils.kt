package com.fivesysdev.weatherapp.utils

object TempUtils {

    fun convertTemp(temp: Double): String {
        return ((temp * (9 / 5)) + 32).toString()
    }
}
//(0 °C × 9/5) + 32