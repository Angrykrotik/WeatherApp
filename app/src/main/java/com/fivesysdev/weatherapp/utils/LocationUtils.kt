package com.fivesysdev.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

object LocationUtils {

    private fun turnOnLocation(activity: Activity) {
        DialogManager.locationSettingDialog(activity, object : DialogManager.Listener {
            override fun onPositive() {
                activity.startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        })
    }

    private fun isLocationEnabled(activity: Activity): Boolean {
        val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getLocation(activity: Activity, locationCallback: ((Double, Double) -> (Unit))) {
        if (!isLocationEnabled(activity)) {
            turnOnLocation(activity)
        } else {
            val cancellationToken = CancellationTokenSource()
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(activity)
            fusedLocationProviderClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationToken.token
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        locationCallback(it.result.latitude, it.result.longitude)
                    }
                }
        }
    }
}

