package com.qihoo360.sedroid;

import java.lang.reflect.Method;

/**
 * Created by chaopei on 2016/2/27.
 * 包含被替换方法和替换方法
 */
public class PatchMethod {
    Method mSrc;
    Method mDst;

    public PatchMethod(Method src, Method dst) {
        mSrc = src;
        mDst = dst;
    }
}
