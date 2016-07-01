package com.zero.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

import java.util.regex.Matcher;

public class TestSms extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestSms";
    
    private static TestSms i;
    
    synchronized public static TestSms getInstance() {
        if (null == i) {
            i = new TestSms();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "用于测试sms权限弹窗";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        ContentValues cv = new ContentValues();
        context.getContentResolver().insert(Uri.parse("content://sms/"), cv);
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor != null) {// 如果短信内容包含“360安全中心”，则做以下处理
            if (cursor.moveToFirst()) {
                do {
                    String smsContentString = cursor.getString(cursor
                            .getColumnIndex("body"));
                    Log.e(TAG, "smsContentString="+smsContentString);
                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
