package com.zero.test;

import java.util.ArrayList;
import java.util.List;

import com.zero.IListData;

public class AMain {
    private static List<IListData> datas = new ArrayList<IListData>();

    static {
        install(TestMultiProcess.getInstance());
        install(TestJni.getInstance());
        install(TestRootJavaProcess.getInstance());
        install(TestMmap.getInstance());
    }
    
    public static List<IListData> testList() {
        return datas;
    }
    
    public static void install(IListData data) {
        datas.add(data);
    }

}
