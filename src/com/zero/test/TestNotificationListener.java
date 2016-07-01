package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestNotificationListener extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestNotificationListener";
    
    private static TestNotificationListener i;
    
    synchronized public static TestNotificationListener getInstance() {
        if (null == i) {
            i = new TestNotificationListener();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "NotificationListenerService";
    }

    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    @Override
    public void clickGo(Context context) {
        context.startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

}
