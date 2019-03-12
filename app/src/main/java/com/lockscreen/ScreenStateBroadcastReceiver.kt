package com.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.application.IApplication

class ScreenStateBroadcastReceiver : BroadcastReceiver {

    var onScreenStateListener : OnScreenStateListener ?= null
    private var hasRegistered = false

    constructor(){
        register()
    }

    private fun register(){
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON) // 开屏
        filter.addAction(Intent.ACTION_SCREEN_OFF) // 锁屏
        filter.addAction(Intent.ACTION_USER_PRESENT) // 解锁
        IApplication.context?.registerReceiver(this , filter)

        hasRegistered = true
    }

    fun unRegister(){
        if (hasRegistered){
            IApplication.context?.unregisterReceiver(this)

            hasRegistered = false
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when(action){
            Intent.ACTION_SCREEN_ON ->{
                //开屏
                onScreenStateListener?.onScreenOn()
            }
            Intent.ACTION_SCREEN_OFF ->{
                //锁屏
                onScreenStateListener?.onScreenOff()
            }
            Intent.ACTION_USER_PRESENT ->{
                //解锁
                onScreenStateListener?.onUserPresent()
            }
        }

    }

}