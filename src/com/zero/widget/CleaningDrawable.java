package com.zero.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by chaopei on 2016/5/27.
 */
public class CleaningDrawable extends Drawable {

    private static final float LINE_WIDTH = 2;

    private static final int LINE_COLOR = Color.rgb(0xff, 0xff, 0xff);

    private Paint mPaint;

    private Biu[] mBius = new Biu[]{
            new Biu(70.0f/*长度*/, 10.0f/*水平位置*/, 255/*透明度*/, 11.0f/*速度*/, 0),
            new Biu(100.0f/*长度*/, 30.0f/*水平位置*/, 160/*透明度*/, 15.0f/*速度*/, 80),
            new Biu(80.0f/*长度*/, 50.0f/*水平位置*/, 120/*透明度*/, 16.0f/*速度*/, 100),
            new Biu(50.0f/*长度*/, 70.0f/*水平位置*/, 50/*透明度*/, 12.0f/*速度*/, 250),
            new Biu(54.0f/*长度*/, 90.0f/*水平位置*/, 200/*透明度*/, 20.0f/*速度*/, 100),
            new Biu(60.0f/*长度*/, 110.0f/*水平位置*/, 255/*透明度*/, 11.0f/*速度*/, 10),
            new Biu(100.0f/*长度*/, 130.0f/*水平位置*/, 160/*透明度*/, 15.0f/*速度*/, 500),
            new Biu(80.0f/*长度*/, 150.0f/*水平位置*/, 120/*透明度*/, 16.0f/*速度*/, 300),
            new Biu(50.0f/*长度*/, 170.0f/*水平位置*/, 50/*透明度*/, 12.0f/*速度*/, 150),
            new Biu(54.0f/*长度*/, 190.0f/*水平位置*/, 200/*透明度*/, 20.0f/*速度*/, 700)
    };

    public CleaningDrawable() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(LINE_WIDTH);
        mPaint.setColor(LINE_COLOR);
    }

    @Override
    public void draw(Canvas canvas) {
        boolean isAllOver = true;
        for (Biu biu : mBius) {
            if (!biu.isInited()) {
                biu.init();
            }
            if (biu.drawNext(canvas, mPaint)) {
                isAllOver = false;
            }
        }
        if (!isAllOver) {
            invalidateSelf();
        }
    }

    public void stop() {
        for (Biu biu : mBius) {
            biu.stop();
        }
    }

    public void start() {
        for (Biu biu : mBius) {
            biu.start();
        }
        invalidateSelf();
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
        return PixelFormat.OPAQUE;
    }

    class Biu {
        private boolean inited = false, loop = true;
        private float upSide;
        private float downSide;
        private int opacity;
        private float length, x, dx, startDy;

        private boolean isVisible() {
            Rect rect = getBounds();
            return downSide >= 0 && upSide <= rect.height();
        }

        boolean isInited() {
            return inited;
        }

        void stop() {
            loop = false;
        }

        void start() {
            loop = true;
        }

        void init() {
            inited = true;
            reset();
        }

        private void reset() {
            Rect rect = getBounds();
            upSide = rect.height() + startDy;
            downSide = upSide + length;
        }

        Biu(float length, float x, int opacity, float dx) {
            this.length = length;
            this.x = x;
            this.startDy = 0;
            this.dx = dx;
            this.opacity = opacity;
        }

        Biu(float length, float x, int opacity, float dx, float startDy) {
            this.length = length;
            this.x = x;
            this.startDy = startDy;
            this.dx = dx;
            this.opacity = opacity;
        }

        Biu(float length, float x, int opacity, float dx, float startDy, boolean loop) {
            this.length = length;
            this.x = x;
            this.startDy = startDy;
            this.dx = dx;
            this.opacity = opacity;
            this.loop = loop;
        }

        boolean drawNext(Canvas canvas, Paint paint) {
            if (isVisible()) {
                paint.setAlpha(opacity);
                canvas.drawLine(x, upSide, x, downSide, paint);
            } else if (!loop) {
                return false;
            }
            upSide -= dx;
            downSide -= dx;
            if (downSide < 0 && loop) {
                reset();
            }
            return true;
        }
    }

}
