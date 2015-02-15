package com.zero.test;

import android.content.Context;

import com.zero.IListData;

public class TestMultiProcess extends IListData{

    private static TestMultiProcess i;
    
    synchronized public static TestMultiProcess getInstance() {
        if (null == i) {
            i = new TestMultiProcess();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "这是一个测试例子";
    }

    @Override
    public void clickGo(Context context) {
        // None
    }

}
