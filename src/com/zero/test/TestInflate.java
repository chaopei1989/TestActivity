package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.inflate.InflateTestActivity;
import com.zero.test.jni.HelloJni;

public class TestInflate extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestInflate";
    
    private static TestInflate i;
    
    synchronized public static TestInflate getInstance() {
        if (null == i) {
            i = new TestInflate();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "inflate 相关的简单例子程序";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.startActivity(new Intent(context, InflateTestActivity.class));
    }

}
