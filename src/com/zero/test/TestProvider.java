package com.zero.test;

import android.animation.TimeAnimator;
import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestProvider extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestProvider";
    
    private static TestProvider i;
    
    synchronized public static TestProvider getInstance() {
        if (null == i) {
            i = new TestProvider();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "ContentProviderClient 相关测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        ContentProviderClient cpc = context.getContentResolver().acquireUnstableContentProviderClient("");
        if(null != cpc)
        try {
            cpc.query(Uri.parse(""), null, null, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        TimeAnimator animator = new TimeAnimator();
        animator.setTimeListener(new TimeAnimator.TimeListener() {
            
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime,
                    long deltaTime) {
                if (DEBUG) {
                    Log.d(TAG, "onTimeUpdate " + totalTime+", " + deltaTime);
                }
            }
        });
        animator.start();
    }
}


