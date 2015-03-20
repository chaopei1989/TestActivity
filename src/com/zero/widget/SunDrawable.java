package com.zero.widget;

import com.zero.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class SunDrawable extends Drawable{

    Context c;
    Paint p;
    
    public SunDrawable(Context c) {
        super();
        this.c=c;
        p = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap sun = BitmapFactory.decodeResource(c.getResources(), R.drawable.sun);
        Rect mSunRect = new Rect(0,0,sun.getWidth(),sun.getHeight());
        canvas.drawBitmap(sun, null, mSunRect, p);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        
    }

    @Override
    public int getOpacity() {
        return 0;
    }

}
