package com.multiprocess.service;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.util.Log;

import com.multiprocess.AppEnv;
import com.multiprocess.crossprocess.Service;
import com.zero.App;

/**
 * Created by chaopei on 2016/4/5.
 */
public class TransactionLargeService extends  ITransactionLarge.Stub {

    private static final boolean DEBUG = AppEnv.DEBUG;

    private static final String TAG = TransactionLargeService.class.getSimpleName();

    public static final int SERVICE_ID = 1;

    private static TransactionLargeService instance;

    final public static Service INSTALLER = new Service() {

        @Override
        public int getServiceId() {
            return SERVICE_ID;
        }

        @Override
        public IBinder getService() {
            return TransactionLargeService.getService();
        }

        @Override
        public IInterface asInterface(IBinder binder) {
            return ITransactionLarge.Stub.asInterface(binder);
        }
    };

    /**
     * 【Server进程】install服务时使用
     *
     * @return
     */
    public static IBinder getService() {
        App.ensureInServerProcess();
        if (null == instance) {
            instance = new TransactionLargeService();
        }
        return instance;
    }


    @Override
    public void transactLargeData(byte[] data) throws RemoteException {
        if(DEBUG) {
            Log.d(TAG, "[transactLargeData] : data.length="+data.length);
        }
    }

    @Override
    public byte[] getTransactLargeData() throws RemoteException {
        byte[] M1 = new byte[1<<21];
//        throw new TransactionTooLargeException();
//        throw new NullPointerException("test nullpointer");
        return M1;
    }

    /*不管是调用端传大数据过去还是被调用端传大数据过来，TransactionTooLargeException总是崩在客户端*/
}
