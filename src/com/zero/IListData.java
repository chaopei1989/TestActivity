package com.zero;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

import com.zero.test.AMain;

public abstract class IListData {
    public String getTitle(){
        return getClass().getSimpleName();
    }

    public abstract String getDesc();

    public void longClickGo(Context context) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  
        vibrator.vibrate(30);
        Toast.makeText(context, getDesc(), Toast.LENGTH_SHORT).show();
    }
    
    public void install(){
        AMain.install(this);
    }

    public abstract void clickGo(Context context);
}

