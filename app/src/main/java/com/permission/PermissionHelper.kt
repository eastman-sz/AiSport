package com.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionHelper {

    companion object {

        //是否有运动需要的权限
        fun hasSportPermission(context: Context) : Boolean{
            return hasAccessFineLocationPermission(context) && hasReadPhoneStatePermission(context)
        }

        fun requestSportPermissions(activity: Activity){
            val list = ArrayList<String>()
            list.addAll(getPermission(activity , Manifest.permission.ACCESS_FINE_LOCATION))
            list.addAll(getPermission(activity , Manifest.permission.READ_PHONE_STATE))
            if (list.isEmpty()){
                return
            }
            val permissions = list.toTypedArray()
            ActivityCompat.requestPermissions(activity, permissions, 1570)
        }

        private fun hasAccessFineLocationPermission(context: Context) : Boolean{
            return hasPermission(context , Manifest.permission.ACCESS_FINE_LOCATION)
        }

        private fun hasReadPhoneStatePermission(context: Context) : Boolean{
            return hasPermission(context , Manifest.permission.READ_PHONE_STATE)
        }

        private fun getPermission(context: Context , permission : String) : List<String>{
            val list = ArrayList<String>()
            if (hasPermission(context , permission)){
                return list
            }
            list.add(permission)
            return list
        }

        //是否有某种权限
        private fun hasPermission(context: Context , permission : String) : Boolean{
            val permission = ContextCompat.checkSelfPermission(context, permission)
            return permission == PackageManager.PERMISSION_GRANTED
        }

    }

}