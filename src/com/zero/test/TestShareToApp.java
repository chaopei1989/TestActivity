package com.zero.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zero.AppEnv;
import com.zero.IListData;

public class TestShareToApp extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestShareToApp";
    
    private static TestShareToApp i;
    
    synchronized public static TestShareToApp getInstance() {
        if (null == i) {
            i = new TestShareToApp();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "分享图片相关的例子";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        List<File> files = new ArrayList<File>();
        share(context, WEIBO, files);
        /*Intent MyIntent = new Intent(Intent.ACTION_SEND);
        MyIntent.setType("image/*");
        MyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(MyIntent);*/
    }

    public static final String QQ = "com.tencent.mobileqq", WEIBO = "com.sina.weibo",
            QZONE = "com.qzone", MOMO = "com.immomo.momo",
            WEIXIN = "com.tencent.mm";

    public static void share(Context context, String pck, String path) {
        if (pck.equals(QQ)) {
            gotoApp(context, QQ);
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(pck);
            intent.setType("image/*");
            if (!TextUtils.isEmpty(path)) {
                File f = new File(path);
                if (f.exists()) {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                }
            }
            if (pck.equals(WEIXIN)) {
                ComponentName comp;
                if (!TextUtils.isEmpty(path)) {
                    comp = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    intent.setComponent(comp);
                }else {
                    gotoApp(context, WEIXIN);
                    return;
                }
            }
            ComponentName cn = intent.resolveActivity(context.getPackageManager());
            Log.d(TAG, cn.getClassName());
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }
    
    public static void share(Context context, String pck) {
        share(context, pck, "");
    }

    public static void gotoApp(Context context, String pck) {
        try {
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(pck);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static void share(Context context, String pck, List<File> files) {
        if (pck.equals(QQ)) {
            gotoApp(context, QQ);
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(pck);
            intent.setType("image/*");
            ArrayList<Uri> uris = new ArrayList<Uri>();
            for (File f : files) {
                Uri u = Uri.fromFile(f);
                uris.add(u);
            }
            if (uris.size() > 0) {
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            }
            if (pck.equals(WEIXIN)) {
                ComponentName comp;
                if (uris.size() > 0) {
                    comp = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    intent.setComponent(comp);
                }else {
                    gotoApp(context, WEIXIN);
                    return;
                }
            }
            ComponentName cn = intent.resolveActivity(context.getPackageManager());
            Log.d(TAG, cn!=null?cn.getClassName():"no match");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }
}
