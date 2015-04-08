package com.zero;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final boolean CRASH_HANDLER_ENABLE = true;
    public static final String TAG = "CrashHandler";
    public static final boolean DEBUG = AppEnv.DEBUG;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE;
    private final Context mContext;

    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String VERSION_BUILD = "versionBuild";
    private static final String OLD_VERSION = "oldVersion";
    private static final String IS_SYSTEM = "isSystem";
    private static final String CID = "cid";
    private static final String STACK_TRACE = "STACK_TRACE";

    private static final String CR_FILE_NAME = "crash_report";
    private static final String CRASH_FOLDER = "crash";
    private static final int TOAST_TIME = 2000;

    private CrashHandler(Context c) {
        mContext = c;
    }

    public static CrashHandler getInstance(Context c) {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler(c);
        }
        return INSTANCE;
    }

    public void init() {
        if (CRASH_HANDLER_ENABLE) {
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (DEBUG) {
            Log.i(TAG, "uncaughtException", new Throwable());
        }
        if (!handleException(ex) && mDefaultHandler != null) {
            if (DEBUG) {
                Log.i(TAG, "uncaughtException default one");
            }
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            if (DEBUG) {
                Log.i(TAG, "exit");
            }
            System.exit(1);
        }
    }

    /*
     * deal with exception ourselves
     */
    private boolean handleException(final Throwable ex) {
        if (DEBUG) {
            Log.i(TAG, "handleException");
        }
        if (ex == null) {
            if (DEBUG) {
                Log.i(TAG, "No exception");
            }
            return true;
        }
        Log.e(TAG, "", ex);
        return true;
    }

    public boolean exists() {
        File dir = getCrashReportFolder();
        return dir != null ? dir.exists() : false;
    }

    private File getCrashReportFolder() {
        File dir = mContext.getFilesDir();
        if (dir != null) {
            return new File(dir.getAbsolutePath(), CRASH_FOLDER);
        } else {
            if (DEBUG) {
                Log.e(TAG, "Can not get files PATH");
            }
            return null;
        }
    }

    /**
     * get crash info
     */
    private void saveCrashInfoToFile(Throwable ex) {
        // 这是在日志中为了标记出现在开始的是 Crash log，不要用 DEBUG 的开关关掉
        Log.e(TAG, "Crash Log BEGIN");// 故意保留的日志，不要放在debug标志里面

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        mDeviceCrashInfo.put(STACK_TRACE, result);
        Log.e(TAG, result);// 故意保留的日志，不要放在debug标志里面
        try {
            File dir = new File(mContext.getFilesDir(), CRASH_FOLDER);
            if (!dir.exists()) {
                dir.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(new File(dir,
                    CR_FILE_NAME));
            fos.write(mDeviceCrashInfo.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);// 故意保留的日志，不要放在debug标志里面
        }

        Log.i(TAG, "Crash Log END");// 故意保留的日志，不要放在debug标志里面
    }


}
