package com.amap.locationservicedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.maps.model.LatLng
import com.amap.gd.LocationService
import com.amap.gd.Utils
import com.gaode.GdMapDrawHelper
import com.gaode.SportParam
import com.sportdata.SportInfoDbHelper
import com.util.ILog
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


        //用于判断异常退出后，是否重绘轨迹
        if (SportParam.sportId == 0L){
            val latestUnCompleteSportId = SportInfoDbHelper.getLatestUnCompleteSportId()
            if (latestUnCompleteSportId > 0L){
                SportParam.sportId = latestUnCompleteSportId
            }
        }

        gdMapDrawHelper = GdMapDrawHelper(mapView.map)

        latLngBroadcastReceive.onLatLngReceiveListener = object : OnLatLngReceiveListener(){
            override fun onReceive(latLng: LatLng , distance : Float) {

                ILog.e("-----------收到GPS点信息--------------------:: " + SportParam.sportId)

                runOnUiThread {
                    gdMapDrawHelper?.drawLine(latLng , 0)

                    simpleSportDataView.setDistance(distance)
                }
            }
            override fun onDurationChg(duration: Int) {
                runOnUiThread {
                    simpleSportDataView.setDuration(duration)
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
        gdMapDrawHelper?.setAppForeground(true)
        simpleSportDataView.setAppForeground(true)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        gdMapDrawHelper?.setAppForeground(false)
        simpleSportDataView.setAppForeground(false)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        latLngBroadcastReceive.unRegister()
        super.onDestroy()
        mapView.onDestroy()
        //stopLocationService()
        SportParam.sportId = 0
    }

    private var pressTimestamp = 0L

    override fun onBackPressed() {
        val timestamp = System.currentTimeMillis()
        if (timestamp - pressTimestamp < 2000){
            stopLocationService()
            super.onBackPressed()
        }
        pressTimestamp = timestamp
    }

}
