package com.zero.test;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestSensor extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestSensor";
    
    private static TestSensor i;
    
    synchronized public static TestSensor getInstance() {
        if (null == i) {
            i = new TestSensor();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "Sensor for test";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

}
