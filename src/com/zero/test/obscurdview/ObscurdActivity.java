package com.zero.test.obscurdview;

import com.zero.R;
import com.zero.SystemUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ObscurdActivity extends Activity implements OnClickListener{
    
    int alpha = 255;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_obscurd);
        findViewById(R.id.change_alpha).setOnClickListener(this);
        findViewById(R.id.change_screen).setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.change_alpha:
            ImageView iv = (ImageView) findViewById(R.id.change_alpha_img);
            alpha = alpha - 10;
            if (alpha > 0) {
                iv.setAlpha(alpha);
            }
            break;

        case R.id.change_screen:
            SystemUtil.setSysScreenBrightness(this, 10);
            break;
        }
        
    }

}
