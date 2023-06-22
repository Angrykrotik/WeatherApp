package com.fivesysdev.weatherapp.utils

import com.fivesysdev.weatherapp.model.LocationInfo
import com.fivesysdev.weatherapp.model.LocationInfoDB

fun LocationInfo.toDB(): LocationInfoDB = LocationInfoDB(lat = lat, lon = lon, name = name, id =(1..10000).random())
fun LocationInfoDB.toInfo(): LocationInfo = LocationInfo(lat = lat, lon = lon, name = name)