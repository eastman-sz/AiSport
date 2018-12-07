package com.gaode

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.zz.sport.ai.R

class GdLocationHelper(var context: Context) : AMapLocationListener{

    private var locationClient: AMapLocationClient? = null
    private var locationOption: AMapLocationClientOption? = null

    private val gdLocationListenerHelper = GdLocationListenerHelper()
    var onGdLocationChangeListener : OnGdLocationChangeListener ?= null

    /**
     * 开始定位
     * @since 2.8.0
     */
    fun startLocation() {
        initLocation()
        // 设置定位参数
        locationClient?.setLocationOption(locationOption)
        // 启动定位
        locationClient?.startLocation()
    }

    /**
     * 停止定位
     * @since 2.8.0
     */
    fun stopLocation() {
        // 停止定位
        locationClient?.stopLocation()
    }

    fun onResume(){
        //切入前台后关闭后台定位功能
        locationClient?.disableBackgroundLocation(true)
    }

    fun onStop2Background(){
        //如果app已经切入到后台，启动后台定位功能
        locationClient?.enableBackgroundLocation(2001, buildNotification())
    }

    /**
     * 初始化定位
     * @since 2.8.0
     */
    private fun initLocation(){
        if (null != locationClient){
            return
        }
        locationClient = AMapLocationClient(context)
        locationOption = getDefaultOption()
        //设置定位参数
        locationClient?.setLocationOption(locationOption)
        // 设置定位监听
        locationClient?.setLocationListener(this)

        //设置回调
        gdLocationListenerHelper.gdLocationChangeListener = onGdLocationChangeListener
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     */
    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode =
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 30000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000//可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = false//可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false//可选，设置是否使用传感器。默认是false
        mOption.isWifiScan = true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
        return mOption
    }

    private val NOTIFICATION_CHANNEL_NAME = "BackgroundLocation"
    private var notificationManager: NotificationManager? = null
    private var isCreateChannel = false

    private fun buildNotification(): Notification? {

        var builder: Notification.Builder? = null
        var notification: Notification? = null
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            val channelId = context.packageName
            if (!isCreateChannel) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.enableLights(true)//是否在桌面icon右上角展示小圆点
                notificationChannel.lightColor = Color.BLUE //小圆点颜色
                notificationChannel.setShowBadge(true) //是否在久按桌面图标时显示此渠道的通知
                notificationManager!!.createNotificationChannel(notificationChannel)
                isCreateChannel = true
            }
            builder = Notification.Builder(context, channelId)
        } else {
            builder = Notification.Builder(context)
        }
        builder.setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("AiSport")
            .setContentText("正在后台运行")
            .setWhen(System.currentTimeMillis())

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build()
        } else {
            return builder.notification
        }
        return notification
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (null == aMapLocation){
            return
        }
        Log.e("sport" , "----------位置信息--${aMapLocation.latitude}   ${aMapLocation.longitude}  ${aMapLocation.accuracy}")

        gdLocationListenerHelper.onLocationChanged(aMapLocation)
    }

}