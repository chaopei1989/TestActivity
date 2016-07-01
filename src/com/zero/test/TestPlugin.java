package com.zero.test;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.Util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class TestPlugin extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestPlugin";
    
    private static TestPlugin i;
    
    synchronized public static TestPlugin getInstance() {
        if (null == i) {
            i = new TestPlugin();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "Dex plugin 相关测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        File pluginFile = new File(context.getFilesDir(), "plugin0.dex");// 总是会刷新
        if (pluginFile.exists()) {
            pluginFile.delete();
        }
        Util.copyAssetToFile(context, "plugin0.dex", pluginFile);
        File optDir = new File(context.getFilesDir(), "opt");
        if (!optDir.isDirectory()) {
            if (optDir.exists()) {
                optDir.delete();
            }
            optDir.mkdir();
        }
        ClassLoader cl = new DexClassLoader(pluginFile.getAbsolutePath(), optDir.getAbsolutePath(), pluginFile.getParent(), context.getClassLoader().getParent());
        try {
            Class<?> clazz = cl.loadClass("com.zero.Rather");
            Method method = clazz.getMethod("than");
            method.invoke(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
