package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.jni.HelloJni;
import com.zero.test.obscurdview.ObscurdActivity;

public class TestObscurdView extends IListData {

    public static final boolean DEBUG = AppEnv.DEBUG;

    private static final String TAG = "TestJni";

    private static TestObscurdView i;

    synchronized public static TestObscurdView getInstance() {
        if (null == i) {
            i = new TestObscurdView();
        }
        return i;
    }

    @Override
    public String getDesc() {
        return "error级别的悬浮窗导致的问题测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.startActivity(new Intent(context, ObscurdActivity.class));
    }

}
