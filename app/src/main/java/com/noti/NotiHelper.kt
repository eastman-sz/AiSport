package com.noti

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.provider.Settings
import com.amap.locationservicedemo.JniMapActivity
import com.application.IApplication
import com.util.DateUtil
import com.utils.lib.ss.common.MathUtil
import com.zz.sport.ai.R

class NotiHelper {

    companion object {

        fun showNotiTest(){
            val msg = Msg()
            msg.title = "测试Title"
            msg.content = "测试内容"

            showNoti(msg)
        }

        private var notificationId = 0
        private var manager: NotificationManager? = null

        /**
         * 应用完全退出，点击事件执行完后会进入主页面(MainActivity)。
         */
        fun showNoti(msg : Msg){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                return
            }
            initNotificationManager()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                //8.0及以上
                val channelId = "AiSportInfo"
                val channelName = "运动信息"
                val importance = NotificationManager.IMPORTANCE_HIGH;

                createNotificationChannel(channelId, channelName, importance)

                notiO(msg)

                return
            }

            notiBelowO(msg)
        }


        //8.0以下
        private fun notiBelowO(msg : Msg){
            val context = IApplication.context

/*            val clickIntent = Intent(context, NotiActivity::class.java)
            clickIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            clickIntent.putExtra("msgId" , msg.mc_id)*/

//            val intents = arrayOf(clickIntent)
//            val contentIntent = PendingIntent.getActivities(IApplication.getContext(), 0, intents, PendingIntent.FLAG_CANCEL_CURRENT)

            val builder = Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(msg.title)
                    .setContentText(msg.content)
//                    .setContentIntent(contentIntent)

            val notification = builder.build()
            notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
            notification.flags = Notification.FLAG_AUTO_CANCEL
            notification.vibrate = longArrayOf(500, 1)

            manager?.notify(notificationId, notification)
        }


        //创建渠道
        @TargetApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(channelId : String , channelName : String , importance : Int){
            val channel = NotificationChannel(channelId , channelName , importance)
            manager?.createNotificationChannel(channel)
        }

        var iRemoteViews : IRemoteViews ?= null

            //8.0及以上通知.必须先创建渠道，然后才能发送通知
        @TargetApi(Build.VERSION_CODES.O)
        private fun notiO(msg : Msg){
                //-------跳转到消息页面---------------
            val channel = manager!!.getNotificationChannel("AiSportInfo")

            if (channel.importance == NotificationManager.IMPORTANCE_NONE){
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, IApplication.context?.packageName);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id);
                IApplication.context?.startActivity(intent)

//                return
            }

            //-------跳转的页面---------------
            val clickIntent = Intent(IApplication.context , NotiActivity::class.java)
            clickIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            val intents = arrayOf(clickIntent)
            val contentIntent = PendingIntent.getActivities(IApplication.context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT)


            if (null == iRemoteViews){
                iRemoteViews = IRemoteViews()
            }

            iRemoteViews?.setTextViewText(R.id.durationTextView , if (0 == msg.duration) "--" else DateUtil.secondsFormatHours1(msg.duration))
            iRemoteViews?.setTextViewText(R.id.distanceTextView , if (0.0 == msg.distance) "--" else MathUtil.meter2KmF(msg.distance.toFloat()).toString())
            iRemoteViews?.setTextViewText(R.id.paceTextView , if (0 == msg.pace) "--" else DateUtil.seconds2RunningPace(msg.pace))

            val notification = NotificationCompat.Builder(IApplication.context!!, "AiSportInfo")
                    .setContentTitle(msg.title)
                    .setContentText(msg.content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setContent(iRemoteViews)
                    .build()
                notification.flags = Notification.FLAG_NO_CLEAR
            manager?.notify(notificationId , notification)
        }

        //8.0及以上 删除某个渠道
        @TargetApi(Build.VERSION_CODES.O)
        private fun deleteNotificationChannel(){
            manager?.deleteNotificationChannel("");
        }

        private fun initNotificationManager() {
            if (null == manager) {
                manager = IApplication.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            while (notificationId > 0) {
//                manager?.cancel(notificationId)
                notificationId--
            }
            notificationId++
        }

        fun cancelNotis(){
            if (null == manager) {
                manager = IApplication.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            while (notificationId > 0) {
                manager?.cancel(notificationId)
                notificationId--
            }
            notificationId++
        }

    }

}