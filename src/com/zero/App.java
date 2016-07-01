package com.zero;

import java.lang.reflect.Method;

import android.app.Application;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.euler.test.A;
import com.qihoo360.sedroid.Sedroid;

public class App extends Application {

    public static final boolean DEBUG = AppEnv.DEBUG;

    public static final String TAG = "ppA";

    /** 未知进程 */
    public static final int PROCESS_TYPE_UNKOWN = 0;
    /** 服务进程 */
    public static final int PROCESS_TYPE_SERVER = 1;
    /** UI进程 */
    public static final int PROCESS_TYPE_UI = 2;

    public static final String SHARED_PREF_FILE = "debug";

    private static int sCurProcessType = PROCESS_TYPE_UNKOWN;

    private static App sInstance;

    public static App getContext() {
        return sInstance;
    }

    public static boolean runInServerProcess() {
        return sCurProcessType == PROCESS_TYPE_SERVER;
    }

    public static boolean runInUiProcess() {
        return sCurProcessType == PROCESS_TYPE_UI;
    }
    
    public static String getProcessName() {
        String process = Util.getCurrentProcessName();
        return process;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        sInstance = this;
        initProcessType();
        CrashHandler.getInstance(this).init();
        //Sedroid patch初始化
//        if (Sedroid.getInstance().setup(this)) {
//            Log.d(TAG, "Sedroid setup done.");
//        }
    }

    private void initProcessType() {
        String process = Util.getCurrentProcessName();
        if (TextUtils.isEmpty(process)
                || !process.startsWith("com.zero")) {
            sCurProcessType = PROCESS_TYPE_UNKOWN;
            Log.e(TAG, "PROCESS_TYPE_UNKOWN", new Exception());
        } else if (process.endsWith(":server")) {
            changeProcessName("AVserver");
            sCurProcessType = PROCESS_TYPE_SERVER;
        } else {
            changeProcessName("AV");
            sCurProcessType = PROCESS_TYPE_UI;
            if (DEBUG) {
                Log.d(TAG, "Process.myUid = " + Process.myUid());
            }
        }
    }

    public static void ensureInServerProcess() {
        if (!runInServerProcess()) {
            throw new RuntimeException("please ensure In SProcess");
        }
    }
    
    /**
     * 有效果的
     * @param v
     */
    private void changeProcessName(String v) {
//        android.os.Process.setArgV0(v);
        
    }
    
    private void changeDdmAppName() {
        if (19 <= Build.VERSION.SDK_INT) {
            try {
                Class clazz = Class.forName("android.ddm.DdmHandleAppName");
                Method method = clazz.getMethod("setAppName", String.class, int.class);
                method.invoke(clazz, "orez.moc", Process.myUid());
                if (DEBUG) {
                    Log.d(TAG, "设置ddms App名 success");
                }
            } catch (Throwable e) {
                if (DEBUG) {
                    Log.e(TAG, "设置ddms进程名", e);
                }
            }
        } else {
//            android.ddm.DdmHandleAppName.setAppName("orez.moc");
        }
    }
    
    @Override
    public void onLowMemory() {
        
        super.onLowMemory();
    }
    
//    @Override
//    public void onTrimMemory(int level) {
//        // TODO Auto-generated method stub
//        super.onTrimMemory(level);
//    }
}
