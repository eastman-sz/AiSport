package com.permission

import android.content.Context
import com.util.GpsHelper
import org.jetbrains.anko.startActivity

class PermissionRequestHelper {

    companion object {

        fun requestSportPermission(context: Context , onLocationPermissionRequestListener : OnLocationPermissionRequestListener?){
            val hasPermission = PermissionHelper.hasSportPermission(context)
            val gpsIsOpen = GpsHelper.gpsIsOpen(context)
            if (hasPermission && gpsIsOpen){
                onLocationPermissionRequestListener?.onGranted()
                return
            }
            PermissionConfig.onLocationPermissionRequestListener = onLocationPermissionRequestListener
            context.startActivity<LocationPermissionActivity>()
        }
    }

}