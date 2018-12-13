package com.amap.locationservicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.amap.api.maps.model.LatLng
import com.application.IApplication
import com.gaode.SportParam

class LatLngBroadcastReceive : BroadcastReceiver {

    var onLatLngReceiveListener : OnLatLngReceiveListener ?= null

    private val latLngAction = "latLngInfo"
    private val actionSportId = "actionSportId"
    private val sportDuration = "sportDuration"

    constructor(){
        register()
    }

    private fun register(){
        val intentFilter = IntentFilter()
        intentFilter.addAction(latLngAction)
        intentFilter.addAction(actionSportId)
        intentFilter.addAction(sportDuration)
        IApplication.context?.registerReceiver(this , intentFilter)
    }

    fun unRegister(){
        IApplication.context?.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            actionSportId ->{
                SportParam.sportId = intent.getLongExtra("sportId" , 0)
            }

            sportDuration ->{
                val duration = intent.getIntExtra("duration" , 0)
                onLatLngReceiveListener?.onDurationChg(duration)

            }

            latLngAction ->{
                val latitude = intent.getDoubleExtra("latitude" , 0.0)
                val longitude = intent.getDoubleExtra("longitude" , 0.0)
                val distance = intent.getFloatExtra("distance" , 0F)

                if (0.0 == latitude || 0.0 === longitude){
                    return
                }

                onLatLngReceiveListener?.onReceive(LatLng(latitude , longitude) , distance)

            }

        }
    }

}