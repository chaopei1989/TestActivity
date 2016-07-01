package com.zero.test;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestOther extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestOther";
    
    private static TestOther i;
    
    synchronized public static TestOther getInstance() {
        if (null == i) {
            i = new TestOther();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "其他测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(new File("/sdcard/DCIM/Camera/ActionShow/video")));
        intent.setType("vnd.android.cursor.dir/video");//视频列表
        context.startActivity(intent);
    }

    
    static class A{
        static{
            Log.d(TestOther.TAG, "A loaded");
        }
        static int m;
    }
    
    static class B extends A{
        static{
            Log.d(TestOther.TAG, "A loaded");
        }
        
        
    }
}

