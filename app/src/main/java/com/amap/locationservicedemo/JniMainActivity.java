package com.amap.locationservicedemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telecom.Connection;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.zz.sport.ai.R;

/**
 * 通过后台服务持续定位
 */
public class JniMainActivity extends Activity {

    private Button buttonStartService;
    private TextView tvResult;

    public static final String RECEIVER_ACTION = "location_in_background";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_main);

        buttonStartService = (Button) findViewById(R.id.button_start_service);
        tvResult = (TextView) findViewById(R.id.tv_result);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (locationChangeBroadcastReceiver != null)
            unregisterReceiver(locationChangeBroadcastReceiver);

        super.onDestroy();
    }

    /**
     * 启动或者关闭定位服务
     *
     */
    public void startService(View v) {
        if (buttonStartService.getText().toString().equals("开始定位")) {
            startLocationService();

            buttonStartService.setText("停止定位");
            tvResult.setText("正在定位...");
        } else {
            stopLocationService();

            buttonStartService.setText("开始定位");
            tvResult.setText("");
        }
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }


    private Connection mLocationServiceConn = null;

    /**
     * 开始定位服务
     */
    private void startLocationService(){
        getApplicationContext().startService(new Intent(this, LocationService.class));
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private void stopLocationService(){
        sendBroadcast(Utils.getCloseBrodecastIntent());
    }

    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVER_ACTION)) {
                String locationResult = intent.getStringExtra("result");
                if (null != locationResult && !locationResult.trim().equals("")) {
                    tvResult.setText(locationResult);
                }
            }
        }
    };

}
