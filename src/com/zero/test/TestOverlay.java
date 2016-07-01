package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.overlay.TestOverlayActivity;

public class TestOverlay extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestOverlay";
    
    private static TestOverlay i;
    
    synchronized public static TestOverlay getInstance() {
        if (null == i) {
            i = new TestOverlay();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "ViewOverlay 相关测试页面";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        Intent intent = new Intent(context, TestOverlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
