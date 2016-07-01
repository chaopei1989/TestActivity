package com.zero.test;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.zero.AppEnv;
import com.zero.IListData;
import com.zero.test.overlay.TestOverlayActivity;

public class TestInputMethod extends IListData{

    public static final boolean DEBUG = AppEnv.DEBUG;
    
    private static final String TAG = "TestInputMethod";
    
    private static TestInputMethod i;
    
    synchronized public static TestInputMethod getInstance() {
        if (null == i) {
            i = new TestInputMethod();
        }
        return i;
    }
    
    @Override
    public String getDesc() {
        return "TestInputMethod 相关测试页面";
    }

    @Override
    public void clickGo(Context context) {
        if (DEBUG) {
            Log.d(TAG, "clickGo");
        }
        H.obtainMessage(0, context).sendToTarget();;
    }
    
    Handler H = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            InputMethodManager imm = (InputMethodManager) ((Context)msg.obj).getSystemService(Context.INPUT_METHOD_SERVICE);
            Log.d(TAG, "imm.isAcceptingText=" + imm.isAcceptingText());
            Log.d(TAG, "imm.isActive=" + imm.isActive());
            Message msg1 = Message .obtain();
            msg1.copyFrom(msg);
            H.sendMessageDelayed(msg1, 1000);
            super.handleMessage(msg);
        }
        
    };

}
