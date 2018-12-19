package com.util

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

class GpsHelper {

    companion object {

        fun gpsIsOpen(context: Context) : Boolean{
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            return isProviderEnabled
        }

        fun openGps(context: Context){
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

    }

}