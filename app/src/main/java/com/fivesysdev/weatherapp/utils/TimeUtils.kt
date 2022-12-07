package com.fivesysdev.weatherapp.utils

import android.text.format.DateFormat
import java.util.*

object TimeUtils {

    fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format("EEE, MMM dd, yyyy", calendar).toString()
    }

    fun getTime(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format("HH:mm a", calendar).toString()
    }

}