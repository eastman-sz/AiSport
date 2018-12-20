package com.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
/**
 * 设备计步器。
 * @author E
 */
public class StepDetectorHelper implements SensorEventListener{

	private Context activity = null;

	private SensorManager sensorManager;
	private Sensor stepCounterSensor = null;
	private Sensor stepDetectorSensor = null;
	//自定义的计步器
	private CustomStepDetector customStepDetector = null;

	private OnSensorChangeListener onSensorChangeListener = null;

	public StepDetectorHelper(Context activity) {
		super();
		this.activity = activity;
		
		registerSensor();
	}
	
	private void registerSensor(){
		sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
		stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		
		if (null != stepCounterSensor) {
			sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
		if (null != stepDetectorSensor) {
			sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
		
		if (null == stepDetectorSensor) {
        	customStepDetector = new CustomStepDetector(activity);
            customStepDetector.setOnSensorChangeListener(onSensorChangeListener);
        	
            Toast.makeText(activity, "Count sensor not available!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void unRegisterSensor(){
		if (null != sensorManager && null != stepCounterSensor) {
			sensorManager.unregisterListener(this, stepCounterSensor);
		}
	    if (null != sensorManager && null != stepDetectorSensor) {
	    	sensorManager.unregisterListener(this, stepDetectorSensor);
		}
		if (null != customStepDetector) {
			customStepDetector.unRegisterSensor();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int length = event.values.length;
		if (length < 1) {
			return;
		}
		float value = event.values[0];
		
		Sensor sensor = event.sensor;
		switch (sensor.getType()) {
		case Sensor.TYPE_STEP_COUNTER:
			int stepCount = (int)value;
			
			if (stepCount > 500000) {
				if (null != sensorManager && null != stepCounterSensor) {
					sensorManager.unregisterListener(this, stepCounterSensor);
				}
			    if (null != sensorManager && null != stepDetectorSensor) {
			    	sensorManager.unregisterListener(this, stepDetectorSensor);
				}
			    //When sensor count exception ,start custom step detecor
				if (null == customStepDetector) {
		        	customStepDetector = new CustomStepDetector(activity);
                    customStepDetector.setOnSensorChangeListener(onSensorChangeListener);
		        	
		            Toast.makeText(activity, "Count sensor not available!", Toast.LENGTH_LONG).show();
				}
				return;
			}
			onSensorChg(1,  stepCount);
			break;
		case Sensor.TYPE_STEP_DETECTOR:
			onSensorChg(2,  (int)value);
			
//			ILog.e("------------------------------步数---------------------------::: " + (int)value);
			break;
		default:
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	public void onStop(){
		unRegisterSensor();
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
