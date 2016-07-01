package com.zero.test.obscurdview;

import com.zero.R;
import com.zero.SystemUtil;
import com.zero.widget.CleaningDrawable;
import com.zero.widget.ProgressDrawable;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ObscurdActivity extends Activity implements OnClickListener{
    
    int alpha = 255;
    int progress = 0;
    ProgressDrawable drawable = new ProgressDrawable();
    CleaningDrawable c = new CleaningDrawable();

    Handler H = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_obscurd);
        findViewById(R.id.change_alpha).setOnClickListener(this);
        findViewById(R.id.change_screen).setOnClickListener(this);
        ((ImageView)findViewById(R.id.change_view)).setImageDrawable(c);
        super.onCreate(savedInstanceState);

        H.postDelayed(new Runnable() {
            @Override
            public void run() {
                c.stop();
            }
        }, 10000);
        H.postDelayed(new Runnable() {
            @Override
            public void run() {
                c.start();
            }
        }, 13000);
        resc();
    }

    public void resc() {
        int a =1;
        int a1 =1;
        int a2 =1;
        int a3 =1;
        int a4 =1;
        int a5 =1;
        int a6 =1;
        int a7 =1;
        int a8 =1;
        int a9 =1;
        int a0 =1;
        Log.v("ObscurdActivity", ""+a+a1+a2+a3+a4+a5+a6+a7+a8+a9+a0);
        resc();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//        case R.id.change_alpha:
//            ImageView iv = (ImageView) findViewById(R.id.change_alpha_img);
//            alpha = alpha - 10;
//            if (alpha > 0) {
//                iv.setAlpha(alpha);
//            }
//            break;
//
        case R.id.change_screen:
//            SystemUtil.setSysScreenBrightness(this, 10);
//            drawable.setProgress(++progress);
            break;
        }
        
    }

}
