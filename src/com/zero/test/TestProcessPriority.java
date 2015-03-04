package com.zero.test;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestProcessPriority extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestProcessPriority";
    
    private static TestProcessPriority i;
    
    synchronized public static TestProcessPriority getInstance() {
        if (null == i) {
            i = new TestProcessPriority();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "进程优先级相关测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);
    }

}
