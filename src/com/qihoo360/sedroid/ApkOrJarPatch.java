package com.qihoo360.sedroid;

import android.content.Context;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import dalvik.system.DexFile;

/**
 * Created by chaopei on 2016/2/26.
 * 一个APK代表一个patch
 */
public class ApkOrJarPatch extends Patch {

    private static final boolean DEBUG = Env.DEBUG;

    private static final String TAG = DEBUG ? "ApkOrJarPatch" : ApkOrJarPatch.class.getSimpleName();

    public static final String ENTRY_NAME = "META-INFO/PATCH.MF";
    public static final String Patch_Name = "Patch-Name";
    public static final String Patch_VersionBuild = "Patch-VersionBuild";
    public static final String Patch_Timestamp = "Patch-Timestamp";
    public static final String Patch_Classes = "Patch-Classes";

    /**
     * 加载它的 ClassLoader
     */
    protected ClassLoader mClassLoader;

    /**
     * patch 所在的路径
     */
    private String mApkOrJarPath;

    /**
     * dexopt/dex2oat 后的 dex 文件
     */
    private String mODexPath;

    /**
     * 加载的 DexFile
     */
    private DexFile mDex;

    /**
     * @param apkOrJarPath    要加载的apk文件路径
     * @param optDexPath 要存储的 odex 文件路径
     * @throws IOException
     */
    public ApkOrJarPatch(Context context, String apkOrJarPath, String optDexPath) throws IOException {
        super(context);
        mApkOrJarPath = apkOrJarPath;
        mODexPath = optDexPath;
        mDex = DexFile.loadDex(mApkOrJarPath, mODexPath, 0);
    }

    @Override
    public void initMetaInfo() {
        JarFile jarFile = null;
        InputStream inputStream = null;
        try {
            // 读取PATCH.MF中的属性
            jarFile = new JarFile(new File(mApkOrJarPath));
            JarEntry entry = jarFile.getJarEntry(ENTRY_NAME);
            inputStream = jarFile.getInputStream(entry);
            Manifest manifest = new Manifest(inputStream);
            Attributes main = manifest.getMainAttributes();
            // patch名
            mName = main.getValue(Patch_Name);
            // patch对应卫士的版本号
            mVersionBuild = main.getValue(Patch_VersionBuild);
            // patch创建的时间戳
            mTimestamp = Long.valueOf(main.getValue(Patch_Timestamp));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Class<?> loadPatchClass(String patchClass) {
        return mDex.loadClass(patchClass, mClassLoader);
    }

    @Override
    public ClassLoader initClassLoader() {
        return new ClassLoader(getClass().getClassLoader()){
            @Override
            protected Class<?> findClass(String className) throws ClassNotFoundException {
                Class<?> clazz = mDex.loadClass(className, mClassLoader);
                if (null == clazz) {
                    throw new ClassNotFoundException(className);
                }
                return clazz;
            }
        };
    }

    @Override
    public String[] initPatchClasses() {
        JarFile jarFile = null;
        InputStream inputStream = null;
        try {
            // 读取PATCH.MF中的属性
            jarFile = new JarFile(new File(mApkOrJarPath));
            JarEntry entry = jarFile.getJarEntry(ENTRY_NAME);
            inputStream = jarFile.getInputStream(entry);
            Manifest manifest = new Manifest(inputStream);
            Attributes main = manifest.getMainAttributes();
            return main.getValue(Patch_Classes).split(",");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isCurrentProcessApply() {
        return true;
    }

}
