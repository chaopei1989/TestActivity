package com.zero.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by chaopei on 2016/5/27.
 */
public class ProgressDrawable extends Drawable {

    private Paint mPaint;

    private final static float FULL_PROGRESS = 100;

    /**
     * @param progress 1-100
     */
    private float mProgress;

    public ProgressDrawable() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setColor(Color.rgb(0xf5, 0x68, 0x60));
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
//        RectF rectF = new RectF(rect.left, rect.top, rect.right * mProgress / FULL_PROGRESS, rect.bottom);
//        canvas.drawRect(rectF, mPaint);
        RectF rectF = new RectF();
        rectF.set(rect);
        rectF.inset(10,10);
        canvas.drawArc(rectF, 270, 0.1f, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }

    /**
     * @param progress 1-100
     */
    public void setProgress(int progress) {
        if (progress< 0 || progress > 100) {
            throw new IllegalArgumentException("progress is error.");
        }
        mProgress = progress;
        invalidateSelf();
    }
}
