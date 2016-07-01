package com.zero.test;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestJudgApkPath extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestJudgApkPath";
    
    private static TestJudgApkPath i;
    
    synchronized public static TestJudgApkPath getInstance() {
        if (null == i) {
            i = new TestJudgApkPath();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "判断apk路径";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        //todo
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.qihoo360.mobilesafe", 0);
            if (DEBUG) {
                Log.e(TAG, "clickGo : info.sourceDir=" + info.sourceDir);
            }
            if (info.sourceDir.startsWith("/system/priv-app")) {
                if (DEBUG) {
                    Log.e(TAG, "is original qiku");
                }
            } else {
                if (DEBUG) {
                    Log.e(TAG, "not qiku or reinstalled");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
