package com.zero.test;

import android.content.Context;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class Test0Empty extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "";
    
    private static Test0Empty i;
    
    synchronized public static Test0Empty getInstance() {
        if (null == i) {
            i = new Test0Empty();
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
        //todo
    }

}
