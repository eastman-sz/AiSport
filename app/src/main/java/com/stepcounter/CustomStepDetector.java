package com.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.util.ILog;

/**
 * 这是一个实现了信号监听的记步的类
 */
public class CustomStepDetector implements SensorEventListener {

	private Context context = null;
	private SensorManager sensorManager = null;
	private Sensor sensor = null;
	
	public static int CURRENT_SETP = 0;
	public static float SENSITIVITY = 3.48f; // SENSITIVITY灵敏度 （值越小越灵敏）
	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;
	/**
	 * 最后加速度方向
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	private OnSensorChangeListener onSensorChangeListener = null;

	/**
	 * 传入上下文的构造函数
	 * 
	 * @param context
	 */
	public CustomStepDetector(Context context) {
		super();
		this.context = context;
		
		int h = 480;
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
		
		CURRENT_SETP = 0;
		
		register();
	}
	
	public void register(){
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void unRegisterSensor(){
		if (null == sensorManager || null == sensor) {
			return;
		}
	    sensorManager.unregisterListener(this, sensor);
	}

	//当传感器检测到的数值发生变化时就会调用这个方法
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				float vSum = 0;
				for (int i = 0; i < 3; i++) {
					final float v = mYOffset + event.values[i] * mScale[1];
					vSum += v;
				}
				int k = 0;
				float v = vSum / 3;

				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {
					// Direction changed
					int extType = (direction > 0 ? 0 : 1); // minumum or
															// maximum?
					mLastExtremes[extType][k] = mLastValues[k];
					float diff = Math.abs(mLastExtremes[extType][k]
							- mLastExtremes[1 - extType][k]);

					if (diff > SENSITIVITY) {
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
						boolean isNotContra = (mLastMatch != 1 - extType);

						if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
								&& isNotContra) {
							end = System.currentTimeMillis();
							if (end - start > 300) {// 此时判断为走了一步-350

								CURRENT_SETP++;
								mLastMatch = extType;
								start = end;
								
								onSensorChg(2, 1);
								//log 步数
								ILog.Companion.e("------pedometer---------: " + CURRENT_SETP);
							}
						} else {
							mLastMatch = -1;
						}
					}
					mLastDiff[k] = diff;
				}
				mLastDirections[k] = direction;
				mLastValues[k] = v;
			}

		}
	}
	
	//当传感器的精度发生变化时就会调用这个方法，在这里没有用
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}
	
	private void onSensorChg(int type , int value){
        if (null == onSensorChangeListener){
            return;
        }
        onSensorChangeListener.onChange(type , value);
	}

    public void setOnSensorChangeListener(OnSensorChangeListener onSensorChangeListener) {
        this.onSensorChangeListener = onSensorChangeListener;
    }
}
