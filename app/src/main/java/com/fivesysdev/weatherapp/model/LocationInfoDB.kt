package com.fivesysdev.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fivesysdev.weatherapp.utils.Constance.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class LocationInfoDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double
)
