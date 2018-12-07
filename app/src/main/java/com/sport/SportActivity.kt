package com.sport

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.LatLng
import com.gaode.*
import com.gaode.service.LocationForegroundService
import com.sportdata.GpsInfoDbHelper
import com.sportdata.SportInfoDbHelper
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_sport.*

class SportActivity : AppCompatActivity() {

    private var serviceIntent : Intent ?= null

    private var gdLocationHelper : GdLocationHelper ?= null
    private var gdMapDrawHelper : GdMapDrawHelper ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)

        title = "运动"

        SportParam.sportId = System.currentTimeMillis()/1000;

        serviceIntent = Intent()
        serviceIntent?.setClass(this , LocationForegroundService::class.java)

        mapView.onCreate(savedInstanceState)

        gdMapDrawHelper = GdMapDrawHelper(mapView.map)

        gdLocationHelper = GdLocationHelper(this)
        gdLocationHelper?.onGdLocationChangeListener = object : OnGdLocationChangeListener{
            override fun onLocationChange(
                latLngState: LatLngState,
                location: AMapLocation,
                latLng: LatLng,
                totalDistance: Float,
                distancePerLatLng: Float) {

                gdMapDrawHelper?.drawLine(latLng , if (latLngState == LatLngState.NORMAL) 0 else 1)

                if (latLngState == LatLngState.NORMAL){
                    GpsInfoDbHelper.save(location)
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            gdLocationHelper?.startLocation()
        } , 2000)

        SportInfoDbHelper.onStart()
    }

    override fun onPause() {
        super.onPause()
        if (null != serviceIntent) {
            startService(serviceIntent)
        }
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        //如果要一直显示可以不执行
        if (null != serviceIntent) {
            stopService(serviceIntent)
        }
        mapView.onResume()
    }

    override fun onDestroy() {
        SportInfoDbHelper.onFinish()
        super.onDestroy()
        //如果要一直显示可以不执行
        if (null != serviceIntent) {
            stopService(serviceIntent)
        }
        mapView.onDestroy()

        gdLocationHelper?.stopLocation()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}
