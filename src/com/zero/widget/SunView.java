package com.zero.widget;

import com.zero.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class SunView extends View{

    Paint p = new Paint();
    
    public SunView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap sun = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
        Rect mSunRect = new Rect(0,0,sun.getWidth(),sun.getHeight());
        canvas.drawBitmap(sun, null, mSunRect, p);
        super.onDraw(canvas);
    }
    
    
}
