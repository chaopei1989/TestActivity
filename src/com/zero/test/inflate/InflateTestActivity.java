package com.zero.test.inflate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.zero.R;

public class InflateTestActivity extends Activity{

    ViewGroup bg_parent, bg;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inflate);
        bg_parent = (ViewGroup) findViewById(R.id.bg_parent);
        bg = (ViewGroup) findViewById(R.id.bg);
        inflate();
        super.onCreate(savedInstanceState);
    }
    
    void inflate(){
        View v;
        
        v = getLayoutInflater().inflate(R.layout.view_inflate, bg_parent);
        
//        v = getLayoutInflater().inflate`(R.layout.view_inflate, null);
        
//        v = getLayoutInflater().inflate(R.layout.view_inflate, bg_parent, false);
        
//        v = getLayoutInflater().inflate(R.layout.view_inflate, bg, false);
        
        
        LayoutParams params = v.getLayoutParams();
//        bg_parent.addView(v);
        
    }
}
