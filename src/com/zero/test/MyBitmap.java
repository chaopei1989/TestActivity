package com.zero.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.graphics.Bitmap;
import android.util.Log;

public class MyBitmap {
    
//    static public void covertBitmapToByte(File f, Bitmap bitmap) throws IOException {
//
//        FileOutputStream fos = new FileOutputStream(f);
//        fos.write(bitmap.mBuffer);
//        fos.close();
//
////        ByteBuffer bb = ByteBuffer.allocate(bitmap.getByteCount());
////        bitmap.copyPixelsToBuffer(bb);
////        bb.rewind();
////        Log.d("MyBitmap", "bb.capacity=" + bb.capacity());
////        FileChannel writeChannel = new FileOutputStream(f, false).getChannel();
////        writeChannel.write(bb);
////        writeChannel.close();
//    }
    
    static public void covertBitmapToByte(File f, Bitmap bitmap, int w, int h) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = bitmap.getPixel(j, i);
                byte b = (byte)(color & 0xff);
                byte g = (byte)((color>>8) & 0xff);
                byte r = (byte)((color>>16) & 0xff);
                byte a = (byte)((color>>24) & 0xff);
                fos.write(r);
                fos.write(g);
                fos.write(b);
                fos.write(a);
            }
        }
        fos.close();
    }

    static public Bitmap createMyBitmap(byte[] data, int width, int height) {
        Log.d("MyBitmap", "data.length="+data.length);
        int[] colors = convertByteToColor(data);
        if (colors == null) {
            return null;
        }
        Log.d("MyBitmap", "colors.length="+colors.length);
        Bitmap bmp = Bitmap.createBitmap(colors, 0, width, width, height,
                Bitmap.Config.ARGB_8888);
        return bmp;
    }

    // 将一个byte数转成int
    // 实现这个函数的目的是为了将byte数当成无符号的变量去转化成int
    public static int convertByteToInt(byte data) {

//        int heightBit = (int) ((data >> 4) & 0x0F);
//        int lowBit = (int) (0x0F & data);
//        return heightBit * 16 + lowBit;
        
        return data & 0xff;
    }

    final static int COLOR_BYTE = 4;
    
    // 将纯RGB数据数组转化成int像素数组
    public static int[] convertByteToColor(byte[] data) {
        int size = data.length; // 1024
        if (size == 0) {
            return null;
        }

        int arg = 0;
        if (size % COLOR_BYTE != 0) {
            arg = 1;
        }

        // 一般情况下data数组的长度应该是4的倍数，这里做个兼容，多余的RGB数据用黑色0XFF000000填充
        int[] color = new int[size / COLOR_BYTE + arg];
        int red, green, blue, alpha;

        if (arg == 0) {
            for (int i = 0; i < color.length; ++i) {
                red = convertByteToInt(data[i * COLOR_BYTE + 0]);
                green = convertByteToInt(data[i * COLOR_BYTE + 1]);
                blue = convertByteToInt(data[i * COLOR_BYTE + 2]);
                alpha = convertByteToInt(data[i * COLOR_BYTE + 3]);
                // 获取RGB分量值通过按位或生成int的像素值
                color[i] = (red << 16) | (green << 8) | blue | (alpha << 24)/*0xFF000000*/;
            }
        } else {
            for (int i = 0; i < color.length - 1; ++i) {
                red = convertByteToInt(data[i * 3]);
                green = convertByteToInt(data[i * 3 + 1]);
                blue = convertByteToInt(data[i * 3 + 2]);
                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }

            color[color.length - 1] = 0xFF000000;
        }

        return color;
    }
}