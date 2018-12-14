package com.permission

import android.app.Activity
import android.os.Bundle

class LocationPermissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasSportPermission = PermissionHelper.hasSportPermission(this)
        if (hasSportPermission){
            PermissionConfig.onLocationPermissionRequestListener?.onGranted()

            finish()
            return
        }
        PermissionHelper.requestSportPermissions(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            1570 -> {
                val hasSportPermission = PermissionHelper.hasSportPermission(this)
                when(hasSportPermission){
                    true -> PermissionConfig.onLocationPermissionRequestListener?.onGranted()
                    false -> PermissionConfig.onLocationPermissionRequestListener?.onDenied()
                }
                finish()
                return
            }
        }
    }

}
