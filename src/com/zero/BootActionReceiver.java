package com.zero;

/**
 * @file BootActionReceiver.java
 * @Synopsis  
 * @author chenshaokun@360.cn
 * @version 1.0
 * @date 2012-04-11
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootActionReceiver extends BroadcastReceiver {
    private static final String TAG = "BootActionReceiver";

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context.getApplicationContext();
        Log.v(TAG, " boot action received ");
        if (intent == null)
            return;
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            while (true) {
            }
        }
    }

}
