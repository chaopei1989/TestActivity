package com.zero.test;

import android.content.Context;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

import java.io.IOException;

public class TestLocalSocket extends IListData implements Runnable {

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestLocalSocket";
    
    private static TestLocalSocket i;
    
    synchronized public static TestLocalSocket getInstance() {
        if (null == i) {
            i = new TestLocalSocket();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return " LocalSocket 相关测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        new Thread(this).start();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            LocalServerSocket lss = new LocalServerSocket("com.zero");
            LocalSocket connect = lss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* 重复使用时会有：
    04-26 15:22:01.939 24211-24329/com.zero W/System.err: java.io.IOException: Address already in use
04-26 15:22:01.939 24211-24329/com.zero W/System.err:     at android.net.LocalSocketImpl.bindLocal(Native Method)
04-26 15:22:01.939 24211-24329/com.zero W/System.err:     at android.net.LocalSocketImpl.bind(LocalSocketImpl.java:306)
04-26 15:22:01.939 24211-24329/com.zero W/System.err:     at android.net.LocalServerSocket.<init>(LocalServerSocket.java:48)
04-26 15:22:01.939 24211-24329/com.zero W/System.err:     at com.zero.test.TestLocalSocket.run(TestLocalSocket.java:45)
04-26 15:22:01.939 24211-24329/com.zero W/System.err:     at java.lang.Thread.run(Thread.java:818)*/
}
