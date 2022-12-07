package com.fivesysdev.weatherapp.utils

import android.app.Activity
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkUtils {

    fun Activity.connectivityManager(): ConnectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    fun Activity.isInternetAvailable(): Boolean {
        val cm = connectivityManager()
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected == true
    }

    fun networkRequest() = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    fun initNetworkListener(callback: ((Boolean) -> (Unit))): ConnectivityManager.NetworkCallback {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback(true)
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                callback(false)
            }
        }
        return networkCallback
    }
}