package com.zero.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

import dalvik.system.DexClassLoader;

public class TestDexOpt extends IListData {

    public static final boolean DEBUG = AppEnv.DEBUG;

    private static final String TAG = "TestDexOpt";

    private static TestDexOpt i;

    synchronized public static TestDexOpt getInstance() {
        if (null == i) {
            i = new TestDexOpt();
        }
        return i;
    }

    @Override
    public String getDesc() {
        return "DexOpt的测试";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        File parent = context.getExternalCacheDir();
        File dexNewFile = new File(parent, "classes.jar");
        if (dexNewFile.exists()) {
            dexNewFile.delete();
        }
        ensureFilesFassetsCopied(context, "classes.jar", dexNewFile.getAbsolutePath());
        if (DEBUG) {
            Log.d(TAG, "DexClassLoader, parent="+parent.getAbsolutePath());
        }
        DexClassLoader classLoader = new DexClassLoader(dexNewFile.getAbsolutePath(), parent.getAbsolutePath(),parent.getAbsolutePath(), context.getClassLoader());
    }

    /**
     * 从assets目录中复制整个文件夹内容
     * 
     * @param context
     *            Context 使用CopyFiles类的Activity
     * @param oldPath
     *            String 原文件路径 如：/aa
     * @param newPath
     *            String 复制后路径 如：xx:/bb/cc
     */
    public void ensureFilesFassetsCopied(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    ensureFilesFassetsCopied(context, oldPath + "/" + fileName,
                            newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
