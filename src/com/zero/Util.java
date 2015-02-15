package com.zero;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class Util {
    
    public static final String TAG = "Util";
    
    /** 返回当前的进程名 */
    public static String getCurrentProcessName() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/self/cmdline")));
            String line = null;
            while ((line = reader.readLine()) != null) {
                return line.trim();
            }
        } catch (Exception e) {
            if (AppEnv.DEBUG)
                Log.e(TAG, "[getCurrentProcessName]: ", e);
        } finally {
            if(reader != null) try {reader.close();} catch (Exception e) { }
        }
        return null;
    }
    
    /** 这是为文件 Copy 准备的方法。 */
    public static void copyStream(InputStream source, OutputStream target)
            throws IOException {
        // Copy
        final int BUF_SIZE = 4096;
        byte[] buffer = new byte[BUF_SIZE];
        int length = 0;
        while ((length = source.read(buffer)) > 0) {
            target.write(buffer, 0, length);
        }
        target.flush();
    }
    
    public static boolean copyAssetToFile(Context c, String name, File target,
            boolean setTimestamp) {
        InputStream sourceStream = null;
        OutputStream targetStream = null;
        try {
            sourceStream = c.getAssets().open(name);
            targetStream = new FileOutputStream(target);

            copyStream(sourceStream, targetStream);

            return true;

        } catch (IOException e) {
            if (AppEnv.DEBUG) {
                Log.e(TAG, "copy asset " + name + " to file " + target.getAbsolutePath() + " failed.", e);
            }
        } finally {
            if (sourceStream != null)
                try {
                    sourceStream.close();
                } catch (Exception e) {
                }
            if (targetStream != null)
                try {
                    targetStream.close();
                } catch (Exception e) {
                }
        }

        return false;
    }
    
    /**
     * 示例：<br>
     *  export CLASSPATH=/data/data/com.zero/files/root_java_process.jar\n<br>
     *  /system/bin/app_process /data/data/com.zero/files com.qihoo360.RFS -fs com.zero\n
     * @param jarPath
     * @param clazz
     * @param arg
     */
    public static void rootRunJar(String jarPath, String clazz, String arg) {
        OutputStream os = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            os = process.getOutputStream();
            String export = String.format("export CLASSPATH=%s\n", jarPath);
            String cmd = String.format("/system/bin/app_process /data/data/com.zero/files %s %s\n", clazz, arg);
            if (AppEnv.DEBUG){
                Log.d(TAG, "[rootRun]: " + export);
                Log.d(TAG, "[rootRun]: " + cmd);
            }
            os.write(export.getBytes());
            os.write(cmd.getBytes());
            os.flush();
        } catch (IOException e) {
            if (AppEnv.DEBUG)
                Log.e(TAG, "[rootRun]: ", e);
        }finally{
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                if (AppEnv.DEBUG)
                    Log.e(TAG, "[rootRun]: ", e);
            }
        }
    }
    
    public static void runPIElibWait(String path, boolean isRoot) {
        File file = new File(path);
        if (file.isFile()) {
            try {
                Log.d(TAG, path);
                Process process;
                if (!isRoot) {
                    process = Runtime.getRuntime().exec(path);
                }else {
                    process = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                    outputStream.writeBytes("./" + path);
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(process.getInputStream())));
                String line = null;
                while (null != (line = br.readLine())) {
                    Log.d(TAG, line);
                }
                process.waitFor();
                Log.d(TAG, "shell process end");
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }else {
            Log.e(TAG, "no so, path="+path);
        }
    }
}
