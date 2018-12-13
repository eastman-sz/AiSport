package com.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 计时器。
 * @author E
 */
public class ScheduleRun {

	private Timer timer = null;
	private int delaytime = 0;
	private boolean onPause = false;
	private int maxCount = Integer.MAX_VALUE;
	private int count = 0;
	
    public ScheduleRun(int delaytime) {
        this.delaytime = delaytime;
    }

    public void start(){
        if (null == timer){
            timer = new Timer();
            timer.schedule(new ScheduleRunTask(), 0, delaytime * 1000);
        }
    }
	
    public void stop() {
        if (null != timer){
            timer.cancel();
            timer = null;
        }

        //重置
        count = 0;
        maxCount = Integer.MAX_VALUE;
        onPause = false;
    }
	
    private class ScheduleRunTask extends TimerTask {
        public void run() {
            if (onPause){
                return;
            }
            count ++;
        	onTimeRun();
        }
    }
    
    private void onTimeRun(){
    	if (null != scheduleRunListener) {
    		scheduleRunListener.onTimeFlip(count , maxCount - count);
		}
    }
    
    public void setScheduleRunListener(ScheduleRunListener scheduleRunListener) {
		this.scheduleRunListener = scheduleRunListener;
	}

	private ScheduleRunListener scheduleRunListener = null;
    
    public interface ScheduleRunListener {
        void onTimeFlip(int count, int leftCount);
    }

    public void pause() {
        this.onPause = true;
    }

    public void resume(){
        this.onPause = false;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
