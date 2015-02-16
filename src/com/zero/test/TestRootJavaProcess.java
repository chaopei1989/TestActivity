package com.zero.test;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.Util;

public class TestRootJavaProcess extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestJni";
    
    private static TestRootJavaProcess i;
    
    private static final String AV_RT = "root_java_process.jar";
    
    synchronized public static TestRootJavaProcess getInstance() {
        if (null == i) {
            i = new TestRootJavaProcess();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "通过 app_process 启动 jar 程序";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        File file = context.getFileStreamPath(AV_RT);// 总是会刷新
        if (file.exists()) {
            file.delete();
        }
        Util.copyAssetToFile(context, AV_RT, file);
        String path = file.getAbsolutePath();
        final String clazz = "com.qihoo360.RFS";
        Util.rootRunJar(path, clazz, "");
    }

}
