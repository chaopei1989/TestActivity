package com.zero.test;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestDefaultApp extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestDefaultApp";
    
    private static TestDefaultApp i;
    
    synchronized public static TestDefaultApp getInstance() {
        if (null == i) {
            i = new TestDefaultApp();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "默认应用程序相关";
    }

    @Override
    public void clickGo(final Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        new Thread(new Runnable() {
        @Override
        public void run() {
            ActivityManager am = (ActivityManager) context.getSystemService("activity");
            while (true) {
                List<RunningTaskInfo> infos = am.getRunningTasks(1);
                if (DEBUG) {
                    Log.d(TAG, "" + infos.get(0).topActivity.getClassName());
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        }).start();
        Intent MyIntent = new Intent(Intent.ACTION_MAIN); 
        MyIntent.addCategory(Intent.CATEGORY_HOME);
//        List list = context.getPackageManager().queryIntentActivities(MyIntent, 0);
        context.startActivity(MyIntent);
    }

}
