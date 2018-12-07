package com.amap.locationservicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.amap.api.maps.model.LatLng
import com.application.IApplication

class LatLngBroadcastReceive : BroadcastReceiver {

    var onLatLngReceiveListener : OnLatLngReceiveListener ?= null

    private val latLngAction = "latLngInfo"

    constructor(){
        register()
    }

    private fun register(){
        val intentFilter = IntentFilter()
        intentFilter.addAction(latLngAction)
        IApplication.context?.registerReceiver(this , intentFilter)
    }

    fun unRegister(){
        IApplication.context?.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            latLngAction ->{
                val latitude = intent.getDoubleExtra("latitude" , 0.0)
                val longitude = intent.getDoubleExtra("longitude" , 0.0)

                if (0.0 == latitude || 0.0 === longitude){
                    return
                }

                onLatLngReceiveListener?.onReceive(LatLng(latitude , longitude))

            }

        }
    }

}