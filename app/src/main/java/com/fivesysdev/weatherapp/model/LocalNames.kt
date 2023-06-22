package com.fivesysdev.weatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalNames(
    val fr: String? = null,
    val ja: String? = null,
    val ru: String? = null,
    val el: String? = null,
    val oc: String? = null,
    val mk: String? = null
): Parcelable
