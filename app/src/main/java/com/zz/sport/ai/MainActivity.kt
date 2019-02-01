package com.zz.sport.ai

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amap.locationservicedemo.JniBroadActivity
import com.history.SportHistoryActivity
import com.lockscreen.LockScreenActivity
import com.lockscreen.OnScreenStateListener
import com.lockscreen.ScreenStateBroadcastReceiver
import com.sport.SportActivity
import com.util.ILog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private var context : Context ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        initListeners()

/*        val screenStateBroadcastReceiver = ScreenStateBroadcastReceiver()
        screenStateBroadcastReceiver.onScreenStateListener = object : OnScreenStateListener{
            override fun onScreenOff() {
                ILog.e("-----------页面----onScreenOff-------------------")

                startActivity(Intent(context , LockScreenActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP))

            }
            override fun onScreenOn() {
                ILog.e("-----------页面----onScreenOn-------------------")
            }
            override fun onUserPresent() {
                ILog.e("-----------页面----onUserPresent-------------------")
            }
        }*/

    }

    private fun initListeners(){
        sportBtn.setOnClickListener {
            startActivity<SportActivity>()
        }

        historyBtn.setOnClickListener {
            startActivity<SportHistoryActivity>()
        }

        jniBtn.setOnClickListener {
            startActivity<JniBroadActivity>()
        }
    }

    override fun onStop() {
        ILog.e("-----------页面----onStop-------------------")
        super.onStop()
    }

}
