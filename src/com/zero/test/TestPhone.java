package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.jni.HelloJni;

public class TestPhone extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestPhone";
    
    private static TestPhone i;
    
    synchronized public static TestPhone getInstance() {
        if (null == i) {
            i = new TestPhone();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "获取手机的相关信息";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        Log.d(TAG, "手机model号：" + Build.MODEL);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.android.phone");
        context.startActivity(intent);
    }

}
