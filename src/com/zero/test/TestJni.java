package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.jni.HelloJni;

public class TestJni extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestJni";
    
    private static TestJni i;
    
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
        context.startActivity(new Intent(context, HelloJni.class));
    }

}
