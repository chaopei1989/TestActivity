package com.zero;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by chaopei on 2016/4/11.
 */
public class OOMTestActivity extends Activity implements OnClickListener {

    private <T extends View> T $(int id) {
        T r = (T)findViewById(id);
        if (r.isClickable()) {
            r.setOnClickListener(this);
        }
        return r;
    }

    ImageView mImg;
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oom);
        mImg = $(R.id.oom_img);
        mBtn = $(R.id.oom_img_change);
    }

    static int count = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oom_img_change:
                if (0 == count++%2) {
                    mImg.setBackgroundResource(R.drawable.wsj2015_2);
                } else {
                    mImg.setBackgroundResource(R.drawable.wsj2015_3);
                }
                break;
        }
    }
}
