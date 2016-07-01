package com.zero.test;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestMusicPlayer extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestMusicPlayer";
    
    private static TestMusicPlayer i;
    
    private Handler handler = new Handler();
    
    synchronized public static TestMusicPlayer getInstance() {
        if (null == i) {
            i = new TestMusicPlayer();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "MusicPlayer相关的简单例子程序";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource("http://s.same.com/track/3843084-c1e0eade.mp3");
            mp.prepare();
            mp.start();
            Toast.makeText(context, "music duration=" + mp.getDuration(), Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
