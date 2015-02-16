package com.zero.test;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.Util;

public class TestMmap extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestMmap";
    
    private static TestMmap i;
    
    private static final String LIB = "/data/data/com.zero/lib/libmongo.so";
    
    private static final String MMAP_FILE = "mongo.db";
    
    synchronized public static TestMmap getInstance() {
        if (null == i) {
            i = new TestMmap();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "利用mmap构建一个简单的MongoDB";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        File file = context.getFileStreamPath(MMAP_FILE);// 总是会刷新
        if (file.exists()) {
            file.delete();
        }
        Util.copyAssetToFile(context, MMAP_FILE, file);
        Util.chmod(file.getAbsolutePath(), "r", true);
        Util.runPIElib(LIB, true, true);
    }

}
