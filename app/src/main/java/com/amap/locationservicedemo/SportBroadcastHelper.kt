package com.amap.locationservicedemo

import android.content.Intent
import com.application.IApplication

class SportBroadcastHelper {

    companion object {

        fun sendSportId(sportId : Long){
            IApplication.context?.sendBroadcast(Intent("actionSportId").putExtra("sportId" , sportId))
        }


    }

}