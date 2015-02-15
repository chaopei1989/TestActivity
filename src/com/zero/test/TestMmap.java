package com.zero.test;

import android.content.Context;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.Util;

public class TestMmap extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestMmap";
    
    private static TestMmap i;
    
    private static final String LIB = "/data/data/com.zero/lib/libmongo.so";
    
    synchronized public static TestMmap getInstance() {
        if (null == i) {
            i = new TestMmap();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "利用mmap构建一个简单的MongoDB";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        Util.runPIElibWait(LIB, false);
    }

}
