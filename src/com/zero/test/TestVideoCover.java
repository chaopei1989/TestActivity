package com.zero.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestVideoCover extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestVideoCover";
    
    private static TestVideoCover i;
    
    synchronized public static TestVideoCover getInstance() {
        if (null == i) {
            i = new TestVideoCover();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "Video Cover";
    }

    @Override
    public void clickGo(Context context) {
        File cover = new File("/sdcard/1437968271070.jpg");
        Bitmap videoBitmap = createVideoThumbnail("/sdcard/1437968271070.mp4");
        try {
            if (cover.isFile()) {
                cover.delete();
                cover.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(cover);
            videoBitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
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
    
    private Bitmap createVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime(0);
        } catch(IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return bitmap;
    }
}
