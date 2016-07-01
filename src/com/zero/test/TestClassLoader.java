package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestClassLoader extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestClassLoader";
    
    private static TestClassLoader i;
    
    synchronized public static TestClassLoader getInstance() {
        if (null == i) {
            i = new TestClassLoader();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "ClassLoader相关的简单例子程序";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.startActivity(new Intent("android.intent.test.Action"));
    }

}
