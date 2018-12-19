package com.permission

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.common.dialog.CommonDialogHelper
import com.common.dialog.OnCommonItemClickListener
import com.util.GpsHelper

class LocationPermissionActivity : Activity() {

    private var context : Context ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context = this

        val hasSportPermission = PermissionHelper.hasSportPermission(this)
        val gpsIsOpen = GpsHelper.gpsIsOpen(this)
        if (hasSportPermission && gpsIsOpen){
            PermissionConfig.onLocationPermissionRequestListener?.onGranted()

            finish()
            return
        }
        if (gpsIsOpen){
            PermissionHelper.requestSportPermissions(this)
        }else{
            //Open gps
            GpsHelper.openGps(this)
        }


    }

    override fun onRestart() {
        super.onRestart()

        //判断GPS是否开启
        val gpsIsOpen = GpsHelper.gpsIsOpen(this)
        if (gpsIsOpen){
            val hasSportPermission = PermissionHelper.hasSportPermission(this)
            if (hasSportPermission){
                PermissionConfig.onLocationPermissionRequestListener?.onGranted()

                finish()
                return
            }else{
                PermissionHelper.requestSportPermissions(this)
            }
        }else{
            //提示开启GPS
            CommonDialogHelper.showCommonTitleDialog(this , "需要打开GPS才能准确记录运动轨迹,是否打开" , "否" , "是" , object : OnCommonItemClickListener<Int>(){
                override fun onItemClick(it: Int) {
                    if (0 == it){
                        PermissionConfig.onLocationPermissionRequestListener?.onDenied()

                        finish()
                        return
                    }else{
                        //Open gps
                        GpsHelper.openGps(context!!)
                    }
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            1570 -> {
                val hasSportPermission = PermissionHelper.hasSportPermission(this)
                when(hasSportPermission){
                    true -> {
                        PermissionConfig.onLocationPermissionRequestListener?.onGranted()
                    }
                    false -> PermissionConfig.onLocationPermissionRequestListener?.onDenied()
                }
                finish()
                return
            }
        }
    }

}
