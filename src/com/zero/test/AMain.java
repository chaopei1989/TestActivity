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
        install(TestProcessPriority.getInstance());
        install(TestObscurdView.getInstance());
        install(TestShareToApp.getInstance());
        install(TestDefaultApp.getInstance());
        install(TestHandler.getInstance());
        install(TestInflate.getInstance());
        install(TestTopActivity.getInstance());
        install(TestOther.getInstance());
        install(TestPhone.getInstance());
        install(TestOverlay.getInstance());
        install(TestMemoryManagement.getInstance());
        install(TestProvider.getInstance());
        install(TestBitmap.getInstance());
        install(TestVideoCover.getInstance());
        install(TestMusicPlayer.getInstance());
        install(TestInputMethod.getInstance());
        install(TestAndfixActivity.getInstance());
        install(TestInstalledApps.getInstance());
        install(TestDexOpt.getInstance());
        install(TestPlugin.getInstance());
        install(TestSensor.getInstance());
        install(TestJudgApkPath.getInstance());
        install(TestStartActivity.getInstance());
        install(TestTransactionTooLarge.getInstance());
        install(TestOOM.getInstance());
        install(TestLocalSocket.getInstance());
        install(TestSms.getInstance());
        install(TestNotificationListener.getInstance());
    }
    
    public static List<IListData> testList() {
        return datas;
    }
    
    public static void install(IListData data) {
        datas.add(data);
    }

}
