package com.fivesysdev.weatherapp.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {


    fun Fragment.checkLocationPermission(onGranded: (() -> (Unit))){
        if(!isPermissionGranded(Manifest.permission.ACCESS_FINE_LOCATION)){
            val permLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                onGranded()
            }
            permLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onGranded()
        }
    }

    fun Fragment.isPermissionGranded(perm: String = Manifest.permission.ACCESS_FINE_LOCATION): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), perm) == PackageManager.PERMISSION_GRANTED
    }
}