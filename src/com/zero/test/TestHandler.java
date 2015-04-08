package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.jni.HelloJni;

public class TestHandler extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestHandler";
    
    private static TestHandler i;
    
    private Handler h = new Handler(){

        @Override
        public void dispatchMessage(Message msg) {
            if (DEBUG) {
                Log.d(TAG, "dispatchMessage");
            }
            super.dispatchMessage(msg);
        }
        
    };
    
    synchronized public static TestHandler getInstance() {
        if (null == i) {
            i = new TestHandler();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "handler相关的简单例子程序";
    }

    @Override
    public void clickGo(Context context) {
        h.sendEmptyMessage(0);
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.startActivity(new Intent(context, HelloJni.class));
    }
}
