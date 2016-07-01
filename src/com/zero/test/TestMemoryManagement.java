package com.zero.test;

import android.app.ActivityManager;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestMemoryManagement extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestMemoryManagement";
    
    private static TestMemoryManagement i;
    
    synchronized public static TestMemoryManagement getInstance() {
        if (null == i) {
            i = new TestMemoryManagement();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "内存管理";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.registerComponentCallbacks(new ComponentCallbacks2() {
            
            @Override
            public void onLowMemory() {
                Log.d(TAG, "onLowMemory");
            }
            
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                Log.d(TAG, "onConfigurationChanged");
            }

            @Override
            public void onTrimMemory(int level) {
                Log.d(TAG, "onTrimMemory");
            }
        });
        context.registerComponentCallbacks(new ComponentCallbacks() {
            
            @Override
            public void onLowMemory() {
                Log.d(TAG, "onLowMemory");
            }
            
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                Log.d(TAG, "onConfigurationChanged");
            }
        });
        
        ActivityManager.RunningAppProcessInfo info = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(info);
        Log.d(TAG, "getMyMemoryState");
    }

}
