package com.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class PermissionHelper {

    companion object {

        //是否有运动需要的权限
        fun hasSportPermission(context: Context) : Boolean{
            return hasAccessFineLocationPermission(context) && hasReadPhoneStatePermission(context)
        }


        fun hasAccessFineLocationPermission(context: Context) : Boolean{
            return hasPermission(context , Manifest.permission.ACCESS_FINE_LOCATION)
        }

        fun hasReadPhoneStatePermission(context: Context) : Boolean{
            return hasPermission(context , Manifest.permission.READ_PHONE_STATE)
        }

        //是否有某种权限
        private fun hasPermission(context: Context , permission : String) : Boolean{
            val permission = ContextCompat.checkSelfPermission(context, permission)
            return permission == PackageManager.PERMISSION_GRANTED
        }

    }

}