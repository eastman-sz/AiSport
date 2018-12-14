package com.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat
import org.jetbrains.anko.startActivity

class PermissionRequestHelper {

    companion object {

        fun requestSportPermission(context: Context , onLocationPermissionRequestListener : OnLocationPermissionRequestListener?){
            val hasPermission = PermissionHelper.hasSportPermission(context)
            if (hasPermission){
                onLocationPermissionRequestListener?.onGranted()
                return
            }
            PermissionConfig.onLocationPermissionRequestListener = onLocationPermissionRequestListener
            context.startActivity<LocationPermissionActivity>()
        }
    }

}