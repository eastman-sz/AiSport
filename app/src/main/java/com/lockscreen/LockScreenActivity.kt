package com.lockscreen

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.amap.api.maps.model.LatLng
import com.amap.locationservicedemo.LatLngBroadcastReceive
import com.amap.locationservicedemo.OnLatLngReceiveListener
import com.util.DateUtil
import com.utils.lib.ss.common.MathUtil
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_lock_screen.*

class LockScreenActivity : Activity() {

    private val latLngBroadcastReceive = LatLngBroadcastReceive()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val win = window
        win.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                )

        setContentView(R.layout.activity_lock_screen)
        title = ""

        init()
    }

    private fun init(){
        initListeners()
        initViews()
    }

    private fun initViews(){
        latLngBroadcastReceive.onLatLngReceiveListener = object : OnLatLngReceiveListener(){
            override fun onDurationChg(duration: Int) {
                runOnUiThread {
                    durationTextView.text = if (0 == duration) "--" else DateUtil.secondsFormatHours1(duration)
                }
            }
            override fun onReceive(latLng: LatLng, distance: Float) {
                runOnUiThread {
                    distanceTextView.text = if (0F == distance) "--" else MathUtil.meter2KmF(distance).toString().plus(" km")
                }
            }
            override fun onPaceChg(pace: Int) {
                runOnUiThread {
                    paceTextView.text = if (0 == pace) "--" else DateUtil.seconds2RunningPace(pace)
                }
            }
        }
    }

    private fun initListeners(){
        coverTextView.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        latLngBroadcastReceive.unRegister()
        super.onDestroy()
    }






}
