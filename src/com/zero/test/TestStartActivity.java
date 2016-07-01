package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestStartActivity extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "";
    
    private static TestStartActivity i;
    
    synchronized public static TestStartActivity getInstance() {
        if (null == i) {
            i = new TestStartActivity();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        Intent intent = new Intent();
        intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        context.startActivity(intent);
    }

}
