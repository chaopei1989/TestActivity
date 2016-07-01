package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.euler.andfix.MainActivity;
import com.zero.AppEnv;
import com.zero.IListData;

public class TestAndfixActivity extends IListData {

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestAndfixActivity";
    
    private static TestAndfixActivity i;
    
    synchronized public static TestAndfixActivity getInstance() {
        if (null == i) {
            i = new TestAndfixActivity();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "AndFix 测试用例";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        context.startActivity(new Intent(context, MainActivity.class));
        Log.d(TAG, "clickGo add");
    }

}
