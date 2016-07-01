package com.qihoo360.sedroid;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.zero.App;
import com.zero.Util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chaopei on 2016/2/25.
 */
public class Sedroid {

    private static final boolean DEBUG = Env.DEBUG;

    private static final String TAG = DEBUG ? "Sedroid" : Sedroid.class.getSimpleName();

    private static List<Patch> sPatches = new ArrayList<Patch>();

    private static boolean isSetuped;

    private Context mContext;

    static {
        try {
            Runtime.getRuntime().loadLibrary("sedroid");
        } catch (Throwable e) {
            if (DEBUG) {
                Log.e(TAG, "loadLibrary", e);
            }
        }
    }
    private static Sedroid sInstance;

    public synchronized static Sedroid getInstance() {
        if (null == sInstance) {
            sInstance = new Sedroid();
        }
        return sInstance;
    }

    synchronized public void addPatch(Patch patch) {
//        if (!isSetuped) {
//            setup();
//        }
        sPatches.add(patch);
        patch.mStatus = Patch.Status.Loaded;
        if (!patch.init()) {
            if (DEBUG) {
                Log.e(TAG, "[addPatch] : patch init failed, name=" + patch.mName);
            }
            return;
        }
        patch.mStatus = Patch.Status.Inited;
        for (String clazz : patch.getPatchClasses()) {
            for (PatchMethod method : patch.getSrcDstMethods(clazz)) {
                replaceMethod(method.mSrc, method.mDst);
                initFields(method.mDst.getDeclaringClass());
            }
        }
        patch.mStatus = Patch.Status.Fixed;
    }

    /**
     * initialize
     *
     * @return true if initialize success
     */
    synchronized public boolean setup(Context context) {
        if (isSetuped) {
            return true;
        }
        mContext = context;
        final String vmVersion = System.getProperty("java.vm.version");
        boolean isArt = vmVersion != null && vmVersion.startsWith("2");
        int apilevel = Build.VERSION.SDK_INT;
        if (DEBUG) {
            Log.e(TAG, "[setup] : Log.classLoader= " + Log.class.getClassLoader());
            Log.e(TAG, "[setup] : vmVersion = " + vmVersion);
            Log.e(TAG, "[setup] : isArt = " + isArt);
            Log.e(TAG, "[setup] : apilevel = " + apilevel);
        }
        if (!setupNative(isArt, apilevel)) {
            if (DEBUG) {
                Log.e(TAG, "[setup] : setupNative failed");
            }
            return false;
        }
        if (!loadPatches()) {
            if (DEBUG) {
                Log.e(TAG, "[setup] : loadPatches failed");
            }
            return false;
        }
        if (!initPatches()) {
            if (DEBUG) {
                Log.e(TAG, "[setup] : initPatches failed");
            }
            return false;
        }
        if (!applyAll()) {
            if (DEBUG) {
                Log.e(TAG, "[setup] : applyAll failed");
            }
            return false;
        }
        isSetuped = true;
        return isSetuped;
    }

    /**
     * 加载所有patch进内存
     */
    private boolean loadPatches() {
        File patchDexFile = new File(App.getContext().getFilesDir().getAbsolutePath() + "/patches", "test_patch.dex");
        if (DEBUG) {
            if (patchDexFile.exists()) {
                patchDexFile.delete();// always refresh for test
            }
            Util.copyAssetToFile(App.getContext(), "patches/test_patch.dex", patchDexFile);
        } else {
            if (!patchDexFile.exists()) {
                //todo 验证可靠性
                Util.copyAssetToFile(App.getContext(), "patches/test_patch.dex", patchDexFile);
            }
        }
        try {
            File odexDir = new File(patchDexFile.getParentFile().getAbsolutePath() + "/opatches/");
            if (!odexDir.isDirectory()) {
                odexDir.mkdirs();
            }
            Patch patch = new DexPatch(mContext, patchDexFile.getAbsolutePath(), odexDir.getAbsolutePath() + "/test_patch.odex");
            sPatches.add(patch);
            patch.mStatus = Patch.Status.Loaded;
            return true;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "[loadPatches]", e);
            }
            return false;
        }
    }

    /**
     * 应用每个patch的修改
     */
    private boolean applyAll() {
        try {
            for (Patch patch : sPatches) {
                if (Patch.Status.Inited == patch.mStatus) {
                    for (Map.Entry<String, List<PatchMethod>> entry : patch.mPatchMethods.entrySet()) {
                        for (PatchMethod method : entry.getValue()) {
                            replaceMethod(method.mSrc, method.mDst);
                            initFields(method.mDst.getDeclaringClass());
                        }
                    }
                    patch.mStatus = Patch.Status.Fixed;
                }
            }
            return true;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "[applyAll]", e);
            }
            return false;
        }
    }

    /**
     * 初始化所有patch中的类、方法
     */
    private boolean initPatches() {
        try {
            for (Patch patch : sPatches) {
                if (!patch.init()) {
                    if (patch.isCurrentProcessApply()) {
                        if (DEBUG) {
                            Log.e(TAG, "[initPatches] : patch init failed, name=" + patch.mName);
                        }
                        return false;
                    }
                    continue;
                }
                patch.mStatus = Patch.Status.Inited;
            }
            return true;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "[initPatches]", e);
            }
            return false;
        }
    }

    /**
     * initialize the target class, and modify access flag of class’ fields to
     * public
     *
     * @param clazz target class
     * @return initialized class
     */
    public static Class<?> initTargetClass(Class<?> clazz) {
        try {
            Class<?> targetClazz = Class.forName(clazz.getName(), true,
                    clazz.getClassLoader());

            initFields(targetClazz);
            return targetClazz;
        } catch (Exception e) {
            Log.e(TAG, "initTargetClass", e);
        }
        return null;
    }

    /**
     * modify access flag of class’ fields to public
     *
     * @param clazz class
     */
    private static void initFields(Class<?> clazz) {
        if (DEBUG) {
            Log.d(TAG, "[initFields]");
        }
        Field[] srcFields = clazz.getDeclaredFields();
        for (Field srcField : srcFields) {
            Log.d(TAG, "[initFields] : modify " + clazz.getName() + "." + srcField.getName()
                    + " flag:");
            setFieldFlagNative(srcField);
        }
    }

    private static void replaceMethod(Method src, Method dst){
        if (DEBUG) {
            Log.d(TAG, "[replaceMethod] ; src = " + src.getDeclaringClass().getName() + "." + src.getName());
            Log.d(TAG, "[replaceMethod] ; dst = " + dst.getDeclaringClass().getName() + "." + dst.getName());
        }
        replaceMethodNative(src, dst);
    }

    private static native boolean setupNative(boolean isArt, int apilevel);

    private static native void replaceMethodNative(Method src, Method dst);

    private static native void setFieldFlagNative(Field field);
}
