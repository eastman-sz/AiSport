package com.gaode.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.sport.SportActivity
import com.zz.sport.ai.R

class LocationForegroundService : Service {

    constructor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Android O上才显示通知栏
        if (Build.VERSION.SDK_INT >= 26) {
            showNotify()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //显示通知栏
    private fun showNotify() {
        val builder = Notification.Builder(applicationContext)
        val nfIntent = Intent(this, SportActivity::class.java)
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("正在后台定位")
            .setContentText("定位进行中")
            .setWhen(System.currentTimeMillis())
        val notification = builder.build()
//        notification.defaults = Notification.DEFAULT_SOUND
        //调用这个方法把服务设置成前台服务
        startForeground(110, notification)
    }

    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    private val mBinder = LocalBinder()

    class LocalBinder : Binder(){
        fun getService() : LocationForegroundService{
            return LocationForegroundService()
        }
    }

}