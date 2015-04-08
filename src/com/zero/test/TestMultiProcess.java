package com.zero.test;

import android.content.Context;
import android.os.RemoteException;

import com.multiprocess.crossprocess.ServiceManager;
import com.multiprocess.service.IStopPackageService;
import com.multiprocess.service.StopPackageService;
import com.zero.IListData;

public class TestMultiProcess extends IListData{

    private static TestMultiProcess i;
    
    synchronized public static TestMultiProcess getInstance() {
        if (null == i) {
            i = new TestMultiProcess();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "这是一个测试例子";
    }

    @Override
    public void clickGo(Context context) {
        IStopPackageService s = (IStopPackageService) ServiceManager.getService(StopPackageService.SERVICE_ID);
        try {
            s.killSysWait();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
