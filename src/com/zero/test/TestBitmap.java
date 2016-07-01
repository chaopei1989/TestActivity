package com.zero.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestBitmap extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestBitmap";
    
    private static TestBitmap i;
    
    synchronized public static TestBitmap getInstance() {
        if (null == i) {
            i = new TestBitmap();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "Bitmap相关的简单例子程序";
    }

    @Override
    public void clickGo(Context context) {
        converBitmapToBytes();
        Bytes2Bitmap();
    }
    
    final static File testBmp = new File("/sdcard/test.png");
    final static File rgbaBytes = new File("/sdcard/rgba_bitmap");
    final static File verifyBmp = new File("/sdcard/verify_test.png");
    
    public void converBitmapToBytes() {
        try {
            rgbaBytes.delete();
            rgbaBytes.createNewFile();
            Bitmap bitmap = BitmapFactory.decodeFile(testBmp.getAbsolutePath());
            MyBitmap.covertBitmapToByte(rgbaBytes, bitmap, L, L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    final static int L = 480;
 // byte[]转换成Bitmap
    public static void Bytes2Bitmap()
    {
        try {
            FileInputStream fis = new FileInputStream(rgbaBytes);
            byte[] buffer = new byte[L*L*32];
            int len = fis.read(buffer);
            Bitmap bitmap = MyBitmap.createMyBitmap(buffer, L, L);
            if (DEBUG) {
                Log.d(TAG, bitmap+"");
            }
            verifyBmp.delete();
            verifyBmp.createNewFile();
            FileOutputStream fos = new FileOutputStream(verifyBmp);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        } catch (IOException e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }
}
