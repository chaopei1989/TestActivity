package com.zero.test.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.zero.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by chaopei on 2016/7/1.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationMonitor extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("SevenNLS", "Notification posted");
        Notification notification = sbn.getNotification();
        File file = Util.createNewFile("/sdcard/big.jpg");
        try {
            notification.largeIcon.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("SevenNLS", "Notification removed");
    }

}