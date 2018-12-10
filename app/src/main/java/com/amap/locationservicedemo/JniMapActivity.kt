package com.amap.locationservicedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.maps.model.LatLng
import com.amap.gd.LocationService
import com.amap.gd.Utils
import com.gaode.GdMapDrawHelper
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_jni_map.*

class JniMapActivity : AppCompatActivity() {

    private var gdMapDrawHelper : GdMapDrawHelper?= null

    private val latLngBroadcastReceive = LatLngBroadcastReceive()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni_map)

        title = "运动"

        mapView.onCreate(savedInstanceState)

        gdMapDrawHelper = GdMapDrawHelper(mapView.map)

        latLngBroadcastReceive.onLatLngReceiveListener = object : OnLatLngReceiveListener{
            override fun onReceive(latLng: LatLng) {

                runOnUiThread {
                    gdMapDrawHelper?.drawLine(latLng , 0)
                }
            }
        }

        startLocationService()
    }

    /**
     * 开始定位服务
     */
    private fun startLocationService() {
        startService(Intent(this, LocationService::class.java))
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private fun stopLocationService() {
        sendBroadcast(Utils.getCloseBrodecastIntent())
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        latLngBroadcastReceive.unRegister()
        super.onDestroy()
        mapView.onDestroy()
        stopLocationService()
    }

}
