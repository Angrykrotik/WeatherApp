package com.fivesysdev.weatherapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationInfo(
    val name: String,
    @SerializedName("local_names")
    val localNames: LocalNames? = null,
    val lat: Double,
    val lon: Double,
    val country: String? = null,
    val state: String? = null
): Parcelable
