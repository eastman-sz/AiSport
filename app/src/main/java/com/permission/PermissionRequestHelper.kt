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

        fun requestSportPermissions(context: Activity){
            val hasAccessFineLocationPermission = PermissionHelper.hasAccessFineLocationPermission(context)
            val hasReadPhoneStatePermission = PermissionHelper.hasReadPhoneStatePermission(context)
            val list = ArrayList<String>()
            if (!hasAccessFineLocationPermission){
                list.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!hasReadPhoneStatePermission){
                list.add(Manifest.permission.READ_PHONE_STATE)
            }
            var permissions: Array<String>? = arrayOf()
            list.toArray(permissions)

            ActivityCompat.requestPermissions(context, permissions!!, 1570)
        }
    }

}