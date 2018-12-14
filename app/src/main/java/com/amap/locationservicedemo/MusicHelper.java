package com.amap.locationservicedemo;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import com.application.IApplication;
import com.zz.sport.ai.R;
/**
 * Music播放辅助类。
 * @author E
 */
public class MusicHelper {

	private MediaPlayer player = null;

    public void play(){
        getMediaPlayer();
        try {
            player.reset();

            AssetFileDescriptor file = IApplication.Companion.getContext().getResources().openRawResourceFd(R.raw.slient);
            player.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            player.prepare();
            player.start();
			player.setLooping(true);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void pause(){
		if (null == player) {
			return;
		}
		if (player.isPlaying()) {
			player.pause();
		}
	}
	
	public void destroy(){
		if (null == player) {
			return;
		}
		player.stop();
		player.release();
		player = null;
	}
	
	private void getMediaPlayer(){
		if (null == player) {
			player = new MediaPlayer();
		}
	}
	
}
