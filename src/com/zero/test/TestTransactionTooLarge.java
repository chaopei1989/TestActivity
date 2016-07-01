package com.zero.test;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.multiprocess.crossprocess.ServiceManager;
import com.multiprocess.service.IStopPackageService;
import com.multiprocess.service.ITransactionLarge;
import com.multiprocess.service.StopPackageService;
import com.multiprocess.service.TransactionLargeService;
import com.zero.AppEnv;
import com.zero.IListData;

import java.io.IOException;
import java.io.InputStream;

public class TestTransactionTooLarge extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestTransactionTooLarge";
    
    private static TestTransactionTooLarge i;
    
    synchronized public static TestTransactionTooLarge getInstance() {
        if (null == i) {
            i = new TestTransactionTooLarge();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "关于 TestTransactionTooLarge 的一个测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        byte[] M1 = new byte[1<<21];

        ITransactionLarge s = (ITransactionLarge) ServiceManager.getService(TransactionLargeService.SERVICE_ID);
        try {
            M1 = s.getTransactLargeData();
//            s.transactLargeData(M1);
            if (DEBUG) {
                Log.d(TAG, "M1.length="+M1.length);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "clietn errrrr", e);
        }
//        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.qihoo360.mobilesafeguard/qiku/nettraffic/enable"), null, null, null, null);
//        if (cursor.moveToFirst()){
//            if (DEBUG) {
//                Log.d(TAG, "clickGo : "+cursor.getString(0));
//                Log.d(TAG, "clickGo : "+cursor.getInt(0));
//                Log.d(TAG, "clickGo : "+cursor.getLong(0));
//            }
//        }

    }

}
