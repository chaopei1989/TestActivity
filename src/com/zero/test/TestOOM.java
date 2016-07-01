package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.OOMTestActivity;

public class TestOOM extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestOOM";
    
    private static TestOOM i;
    
    synchronized public static TestOOM getInstance() {
        if (null == i) {
            i = new TestOOM();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "OOM 的一个测试页面";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.startActivity(new Intent(context, OOMTestActivity.class));
    }

}
