package com.zero.test;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;

import com.zero.App;
import com.zero.AppEnv;
import com.zero.IListData;

public class TestTopActivity extends IListData implements Runnable{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestTopActivity";
    
    private static TestTopActivity i;
    
    synchronized public static TestTopActivity getInstance() {
        if (null == i) {
            i = new TestTopActivity();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "获取top Activity";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        if (th == null) {
            th = new Thread(this);
            th.start();
        } else {
            if (stop) {
                stop = false;
            }else {
                stop = true;
            }
        }
    }

    Thread th;
    
    volatile boolean stop = false;
    
    @Override
    public void run() {
        ActivityManager am = (ActivityManager) App.getContext().getSystemService("activity");
        while (!stop) {
            List<RunningTaskInfo> list = am.getRunningTasks(1);
            if (null != list && 0 < list.size()) {
                RunningTaskInfo info = list.get(0);
                Log.d(TAG, "pkg=" + info.topActivity.getPackageName());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
