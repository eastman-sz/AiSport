package com.amap.locationservicedemo

import android.content.Intent
import com.application.IApplication

class SportBroadcastHelper {

    companion object {

        private val sportIdIntent = Intent("actionSportId")
        private val durationIntent = Intent("sportDuration")
        private val latLngIntent = Intent("latLngInfo")

        fun sendSportId(sportId : Long){
            IApplication.context?.sendBroadcast(sportIdIntent.putExtra("sportId" , sportId))
        }

        fun sendDuration(duration : Int){
            IApplication.context?.sendBroadcast(durationIntent.putExtra("duration" , duration))
        }

        fun sendLatLng(latitude : Double , longitude : Double , distance : Float){
            IApplication.context?.sendBroadcast(
                latLngIntent.putExtra("latitude", latitude)
                    .putExtra("longitude", longitude)
                    .putExtra("distance" , distance)
            )
        }


    }

}