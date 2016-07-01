package com.qihoo360.sedroid;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.qihoo360.sedroid.annotation.MethodFix;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaopei on 2016/2/27.
 * <p/>
 * Patch 抽象类
 */
abstract public class Patch {

    private static final boolean DEBUG = Env.DEBUG;

    private static final String TAG = DEBUG ? "Patch" : Patch.class.getSimpleName();

    public enum Status {
        Unloaded, Loaded, Inited, Fixed
    }

    protected Context mContext;

    /**
     * 加载它的 ClassLoader
     */
    protected ClassLoader mClassLoader;

    public static final String Default_Patch_Name = "Unknown";

    protected String mName = Default_Patch_Name;

    protected long mTimestamp;

    protected String mVersionBuild;

    protected Status mStatus = Status.Unloaded;

    /**
     * patch 中包含替换方法的类
     */
    protected String[] mPatchClasses = null;

    /**
     * 被替换方法与替换成的方法，Key是被替换的类
     */
    protected Map<String, List<PatchMethod>> mPatchMethods = new HashMap<>();

    /**
     * 初始化patch基本信息
     *
     * @return
     */
    abstract public void initMetaInfo();

    abstract public Class<?> loadPatchClass(String patchClass);

    abstract public ClassLoader initClassLoader();

    abstract public String[] initPatchClasses();

    abstract public boolean isCurrentProcessApply();

    public Patch(Context context) {
        mContext = context;
    }

    /**
     * 解析出包含patch目标方法的类
     *
     * @return
     */
    private boolean initPatchClass(String patchClass) {
        try {
            // 加载该patch class但没有初始化，我们默认写入配置的class一定存在，不存在一定是bug
            Class<?> dstClazz = loadPatchClass(patchClass);
            if (null == dstClazz) {
                return false;
            }
            Method[] dstClazzAllMethods = dstClazz.getDeclaredMethods();
            // 标注中包含被替换方法的信息
            MethodFix srcMethodFixAnnotaion;
            for (Method dstMethod : dstClazzAllMethods) {
                // 找到替换方法
                srcMethodFixAnnotaion = dstMethod.getAnnotation(MethodFix.class);
                if (srcMethodFixAnnotaion == null) {
                    continue;
                }
                // 标注中的被修改的类
                String clz = srcMethodFixAnnotaion.clazz();
                List<PatchMethod> methods;
                if (null == (methods = mPatchMethods.get(clz))) {
                    methods = new ArrayList<>();
                    mPatchMethods.put(clz, methods);
                }
                // 标注中的被修改的方法
                String meth = srcMethodFixAnnotaion.method();
                Class<?> srcClazz = Sedroid.initTargetClass(Class.forName(clz));
                if (null != srcClazz && !TextUtils.isEmpty(meth)) {
                    if (DEBUG) {
                        Log.d(TAG, "[init] : src clz= " + clz);
                        Log.d(TAG, "[init] : src meth= " + meth);
                    }
                    Method srcMethod = srcClazz.getDeclaredMethod(meth, dstMethod.getParameterTypes());
                    methods.add(new PatchMethod(srcMethod, dstMethod));
                } else {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean init() {
        mClassLoader = initClassLoader();
        initMetaInfo();
        if (!isCurrentProcessApply()) {
            return false;
        }
        try {
            // patch中哪些类是包含被替换方法的
            String[] classStrs = initPatchClasses();
            for (String str : classStrs) {
                String classStr = str.trim();
                if (!TextUtils.isEmpty(classStr)) {
                    if (!initPatchClass(classStr)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //////////////////////////////////Getters/////////////////////////////////////

    public List<PatchMethod> getSrcDstMethods(String srcClass) {
        return mPatchMethods.get(srcClass);
    }

    public String[] getPatchClasses() {
        return mPatchClasses;
    }

    public Status getStatus() {
        return mStatus;
    }

    public String getPatchName() {
        return mName;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    /**
     * @return 返回该patch对应的版本号+build号，如 6.3.0.1234
     */
    public String getVersionBuild() {
        return mVersionBuild;
    }
}
