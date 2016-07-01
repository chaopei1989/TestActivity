package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestJni extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestJni";
    
    private static TestJni i;
    
    private Handler handler = new Handler();
    
    synchronized public static TestJni getInstance() {
        if (null == i) {
            i = new TestJni();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "JNI相关的简单例子程序";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
//        context.startActivity(new Intent("android.intent.test.Action"));
        handler.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                if (DEBUG) {
                    Log.d(TAG, "delay clickGo");
                }
            }
        }, 2000);
    }

}
