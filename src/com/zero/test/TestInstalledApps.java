package com.zero.test;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zero.App;
import com.zero.AppEnv;
import com.zero.IListData;

public class TestInstalledApps extends IListData {

    public static final boolean DEBUG = AppEnv.DEBUG;

    private static final String TAG = "TestInstalledApps";

    private static TestInstalledApps i;

    synchronized public static TestInstalledApps getInstance() {
        if (null == i) {
            i = new TestInstalledApps();
        }
        return i;
    }

    @Override
    public String getDesc() {
        return "获取所有安装的应用";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        PackageManager pm = App.getContext().getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (int i = 0; i < infos.size(); i++) {
            PackageInfo info = infos.get(i);
            Log.d(TAG, "appName: " + pm.getApplicationLabel(info.applicationInfo) + ", flag: "+(info.applicationInfo.flags&1));
            
        }

    }

}
