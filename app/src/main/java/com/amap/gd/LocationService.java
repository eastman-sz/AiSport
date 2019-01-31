package com.amap.gd;

import android.content.Intent;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.locationservicedemo.*;
import com.cal.SportDataCalHelper;
import com.gaode.GdLocationListenerHelper;
import com.gaode.LatLngState;
import com.gaode.OnGdLocationChangeListener;
import com.gaode.SportParam;
import com.noti.Msg;
import com.noti.NotiHelper;
import com.sportdata.GpsInfoDbHelper;
import com.sportdata.SportInfo;
import com.sportdata.SportInfoDbHelper;
import com.stepcounter.OnSensorChangeListener;
import com.stepcounter.StepDetectorHelper;
import com.util.ILog;
import com.util.ScheduleRun;
import org.jetbrains.annotations.NotNull;
/**
 * <p>
 * 创建时间：2016/10/27
 * 项目名称：LocationServiceDemo
 * <p>
 * 类说明：后台服务定位
 * <p>
 *     update:
 *     1. 只有在由息屏造成的网络断开造成的定位失败时才点亮屏幕
 *     2. 利用notification机制增加进程优先级
 * </p>
 */
public class LocationService extends NotiService {

    private AMapLocationClient mLocationClient;

    private int locationCount;

    /**
     * 处理息屏关掉wifi的delegate类
     */
    private IWifiAutoCloseDelegate mWifiAutoCloseDelegate = new WifiAutoCloseDelegate();

    /**
     * 记录是否需要对息屏关掉wifi的情况进行处理
     */
    private boolean mIsWifiCloseable = false;

    private GdLocationListenerHelper gdLocationListenerHelper = new GdLocationListenerHelper();

    //运动ID
    private long sportId = 0;
    //计时器
    private ScheduleRun scheduleRun = new ScheduleRun(1);
    //计算器
    private SportDataCalHelper sportDataCalHelper = new SportDataCalHelper();
    //计步器
    private StepDetectorHelper stepDetectorHelper = null;

    private Msg msg = new Msg();

    @Override
    public void onCreate() {
        super.onCreate();

        scheduleRun.setScheduleRunListener(new ScheduleRun.ScheduleRunListener() {
            @Override
            public void onTimeFlip(int count, int leftCount) {
                ILog.Companion.e("--------时长-------------: " + count +  "  : " + sportId);
                //发送
                SportBroadcastHelper.Companion.sendDuration(count);
                //更新
                SportInfoDbHelper.Companion.onUpdate();
                //更新每公里信息
                sportDataCalHelper.onDurationChange(count);

                //更新通知栏
                if (0 == count%5){
                    msg.setDuration(count);
                    msg.setDistance(sportDataCalHelper.getDistance());
                    msg.setPace(sportDataCalHelper.getPace());

                    NotiHelper.Companion.showNoti(msg);
                }

            }
        });
        scheduleRun.start();

        gdLocationListenerHelper.setGdLocationChangeListener(new OnGdLocationChangeListener() {
            @Override
            public void onLocationChange(@NotNull LatLngState latLngState, @NotNull AMapLocation location, @NotNull LatLng latLng, float totalDistance, float distancePerLatLng) {
                if (latLngState == LatLngState.NORMAL){
                    //正常点
                    SportBroadcastHelper.Companion.sendLatLng(location.getLatitude() , location.getLongitude() , totalDistance);

                    ILog.Companion.e("-----------保存GPS点信息--------------------:: " + SportParam.Companion.getSportId()  + "    totalDistance: " + totalDistance);
                    //send
                    SportBroadcastHelper.Companion.sendSportId(SportParam.Companion.getSportId());
                    //保存GPS点
                    GpsInfoDbHelper.Companion.save(location , sportDataCalHelper.getPace());
                    //更新距离
                    SportInfoDbHelper.Companion.onUpdateDistance(totalDistance);
                    //更新每公里信息
                    sportDataCalHelper.onDistanceChange(totalDistance , latLng);
                    //更新配速信息
                    SportInfoDbHelper.Companion.onUpdatePaceInfo(sportDataCalHelper.getMinPace() , sportDataCalHelper.getMaxPace());
                }
            }
        });

        stepDetectorHelper = new StepDetectorHelper(getApplicationContext());
        stepDetectorHelper.setOnSensorChangeListener(new OnSensorChangeListener() {
            @Override
            public void onChange(int type, int value) {
                if (2 == type){
                    sportDataCalHelper.onStepsChange(value);
                }
            }
        });
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        applyNotiKeepMech(); //开启利用notification提高进程优先级的机制

        if (mWifiAutoCloseDelegate.isUseful(getApplicationContext())) {
            mIsWifiCloseable = true;
            mWifiAutoCloseDelegate.initOnServiceStarted(getApplicationContext());
        }

        ILog.Companion.e("----------服务唤起--------------: " + sportId);

        //SportId
        if (0 == sportId){
            sportId = System.currentTimeMillis()/1000;
        }

        SportParam.Companion.setSportId(sportId);
        SportInfoDbHelper.Companion.onStart();
        //send
        SportBroadcastHelper.Companion.sendSportId(SportParam.Companion.getSportId());

        startLocation();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unApplyNotiKeepMech();
        stopLocation();
        scheduleRun.stop();
        //结束运动记录
        SportInfoDbHelper.Companion.onFinish();

        //调用stopSelf()会重置LocationService里的变量
        stopSelf();
        //停止计步器
        stepDetectorHelper.onStop();

        //取消通知栏
        NotiHelper.Companion.cancelNotis();
        super.onDestroy();
    }

    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();

        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        mLocationOption.setLocationCacheEnable(false);
        // 每10秒定位一次
        mLocationOption.setInterval(2 * 1000);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //发送结果的通知
            sendLocationBroadcast(aMapLocation);

            if (!mIsWifiCloseable) {
                return;
            }

            if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                mWifiAutoCloseDelegate.onLocateSuccess(getApplicationContext(), PowerManagerUtil.getInstance().isScreenOn(getApplicationContext()), NetUtil.getInstance().isMobileAva(getApplicationContext()));
            } else {
                mWifiAutoCloseDelegate.onLocateFail(getApplicationContext() , aMapLocation.getErrorCode() , PowerManagerUtil.getInstance().isScreenOn(getApplicationContext()), NetUtil.getInstance().isWifiCon(getApplicationContext()));
            }

        }

        private void sendLocationBroadcast(AMapLocation aMapLocation) {
            //记录信息并发送广播
            locationCount++;
            long callBackTime = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();
            sb.append("定位完成 第" + locationCount + "次\n");
            sb.append("回调时间: " + Utils.formatUTC(callBackTime, null) + "\n");
            if (null == aMapLocation) {
                sb.append("定位失败：location is null!!!!!!!");
            } else {
                sb.append(Utils.getLocationStr(aMapLocation));
            }
//            Log.e("location" , "----------定位信息--------------: " + sb.toString());

            Intent mIntent = new Intent(JniMainActivity.RECEIVER_ACTION);
            mIntent.putExtra("result", sb.toString());

            //发送广播
            sendBroadcast(mIntent);

            //分解
            if (null != gdLocationListenerHelper){
                gdLocationListenerHelper.onLocationChanged(aMapLocation);
            }

        }
    };
}
