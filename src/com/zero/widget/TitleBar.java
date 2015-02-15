package com.zero.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.App;
import com.zero.R;

/**
 * 新版TitleBar, 暂时保留tip。预置返回键、标题、右侧的textButton和MenuButton，右侧按钮最多显示一个，默认都不显示。
 * <p>
 * 自定义属性：<br>
 * <b>av_btnLeftVisible</b> - back键是否显示，默认为true<br>
 * <b>av_titleText</b> - 标题文本<br>
 * <b>av_btnTextVisible</b> - 右侧文本按钮是否显示，默认为false<br>
 * <b>av_btnTextBackground</b> - 设置右侧文本按钮背景<br>
 * <b>av_btnTextString</b> - 设置右侧文本按钮的文本<br>
 * <b>av_btnMenuVisible</b> - 右侧菜单按钮是否显示，默认为false<br>
 * <b>av_btnMenuSrc</b> - 右侧菜单按钮显示图<br>
 * <b>av_btnMenuBackground</b> - 右侧菜单按钮背景图，一般不用设置<br>
 * <b>av_titlebarBackground</b> - 标题背景
 * 
 * @author chaopei
 */
public class TitleBar extends LinearLayout {

    private static final boolean DEBUG = App.DEBUG;

    private static final String TAG = DEBUG ? "TitleBar" : TitleBar.class
            .getSimpleName();

    private ViewGroup mContainer;

    /**
     * 标题
     */
    public final TextView title;

    /**
     * 返回按钮
     */
    public final ImageView backBtn;

    /**
     * 右侧menu按钮，可以通过设置src来改变显示图，可以通过设置background来改变背景图
     */
    public final ImageView rightMenuBtn;

    /**
     * 右侧text按钮，可以通过设置background来改变背景图
     */
    public final TextView rightTextBtn;
    
    public TitleBar(final Context context) {
        super(context);
        inflate(context, R.layout.av_widget_title_bar_content_v2, this);
        backBtn = (ImageView) findViewById(R.id.title_bar_back);
        title = (TextView) findViewById(R.id.title_bar_title);
        rightTextBtn = (TextView) findViewById(R.id.title_bar_btn);
        rightMenuBtn = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mContainer = (ViewGroup) findViewById(R.id.titlebar_parent);
        if (context instanceof Activity) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity a = (Activity) context;
                    a.finish();
                }
            });
        }
    }

    public TitleBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.av_widget_title_bar_content_v2, this);
        backBtn = (ImageView) findViewById(R.id.title_bar_back);
        title = (TextView) findViewById(R.id.title_bar_title);
        rightTextBtn = (TextView) findViewById(R.id.title_bar_btn);
        rightMenuBtn = (ImageView) findViewById(R.id.title_bar_menu_btn);
        mContainer = (ViewGroup) findViewById(R.id.titlebar_parent);

        /*
         * mLeftBtnPadding[0] = backBtn.getPaddingLeft(); mLeftBtnPadding[1] =
         * backBtn.getPaddingTop(); mLeftBtnPadding[2] =
         * backBtn.getPaddingRight(); mLeftBtnPadding[3] =
         * backBtn.getPaddingBottom();
         * 
         * mRightBtnPadding[0] = rightTextBtn.getPaddingLeft();
         * mRightBtnPadding[1] = rightTextBtn.getPaddingTop();
         * mRightBtnPadding[2] = rightTextBtn.getPaddingRight();
         * mRightBtnPadding[3] = rightTextBtn.getPaddingBottom();
         */

        TypedArray t = context.obtainStyledAttributes(attrs,
                R.styleable.AV_Shield_TitleBar);
        boolean showBackBtn = t.getBoolean(
                R.styleable.AV_Shield_TitleBar_av_btnLeftVisible, true);
        if (showBackBtn) {
            backBtn.setVisibility(View.VISIBLE);
        } else {
            backBtn.setVisibility(View.GONE);
        }
        boolean showMenuBtn = t.getBoolean(
                R.styleable.AV_Shield_TitleBar_av_btnMenuVisible, false);
        boolean showTextBtn = t.getBoolean(
                R.styleable.AV_Shield_TitleBar_av_btnTextVisible, false);
        if (DEBUG) {
            Log.d(TAG, "[TitleBar] : showMenuBtn=" + showMenuBtn);
        }
        if (showMenuBtn) {
            rightMenuBtn.setVisibility(View.VISIBLE);
            rightTextBtn.setVisibility(View.GONE);
        } else {
            rightMenuBtn.setVisibility(View.GONE);
        }

        if (showTextBtn) {
            rightTextBtn.setVisibility(View.VISIBLE);
            rightMenuBtn.setVisibility(View.GONE);
        } else {
            rightTextBtn.setVisibility(View.GONE);
        }

        rightTextBtn.setText(t
                .getString(R.styleable.AV_Shield_TitleBar_av_btnTextString));

        Drawable rightMenuSrc = t
                .getDrawable(R.styleable.AV_Shield_TitleBar_av_btnMenuSrc);
        if (null != rightMenuSrc) {
            rightMenuBtn.setImageDrawable(rightMenuSrc);
        }
        Drawable rightMenuBg = t
                .getDrawable(R.styleable.AV_Shield_TitleBar_av_btnMenuBackground);
        if (null != rightMenuBg) {
            rightMenuBtn.setBackgroundDrawable(rightMenuBg);
        }
        Drawable rightTextBg = t
                .getDrawable(R.styleable.AV_Shield_TitleBar_av_btnTextBackground);
        if (null != rightMenuSrc) {
            rightTextBtn.setBackgroundDrawable(rightTextBg);
        }
        /*
         * Drawable rightBrnBg =
         * t.getDrawable(R.styleable.AV_Shield_TitleBar_av_btnRightBackground);
         * if (rightBrnBg != null) { menuBtn.setBackgroundDrawable(rightBrnBg);
         * menuBtn.setPadding(mRightBtnPadding[0], mRightBtnPadding[1],
         * mRightBtnPadding[2], mRightBtnPadding[3]); }
         * 
         * Drawable leftBrnBg =
         * t.getDrawable(R.styleable.AV_Shield_TitleBar_av_btnLeftBackground);
         * if (leftBrnBg != null) { backBtn.setBackgroundDrawable(leftBrnBg);
         * backBtn.setPadding(mLeftBtnPadding[0], mLeftBtnPadding[1],
         * mLeftBtnPadding[2], mLeftBtnPadding[3]); }
         */
        Drawable titleBg = t
                .getDrawable(R.styleable.AV_Shield_TitleBar_av_titlebarBackground);
        if (titleBg != null) {
            mContainer.setBackgroundDrawable(titleBg);
        }

        title.setText(t.getText(R.styleable.AV_Shield_TitleBar_av_titleText));
        t.recycle();
        if (context instanceof Activity) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity a = (Activity) context;
                    a.finish();
                }
            });
        }
    }

    /**
     * 名字兼容卫士的CommonDialog，设置右方按钮的点击事件，只会对右方显示的按钮有效
     * 
     * @param listener
     */
    public void setOnSettingListener(OnClickListener listener) {
        if (View.VISIBLE == rightMenuBtn.getVisibility()) {
            rightMenuBtn.setOnClickListener(listener);
        } else if (View.VISIBLE == rightTextBtn.getVisibility()) {
            rightTextBtn.setOnClickListener(listener);
        }
    }

    public void setTitle(int txt) {
        this.title.setText(txt);
    }

    public void setTitle(CharSequence txt) {
        this.title.setText(txt);
    }

    public void setMiddleView(View v) {
        setView(R.id.common_ll_middle, v);
    }

    public void setMiddleView(int layoutResId) {
        setView(R.id.common_ll_middle, layoutResId);
    }

    private void setView(int rootId, View v) {
        LinearLayout ll = (LinearLayout) findViewById(rootId);
        ll.removeAllViews();
        ll.addView(v);
    }

    private void setView(int rootId, int layoutId) {
        LinearLayout ll = (LinearLayout) findViewById(rootId);
        ll.removeAllViews();
        inflate(getContext(), layoutId, ll);
    }

    public void setBackGround(Drawable bg) {
        mContainer.setBackgroundDrawable(bg);
    }

    public void setBackGround(int bg) {
        mContainer.setBackgroundResource(bg);
    }

    public void setSettingTxt(int text) {
        rightTextBtn.setText(text);
    }

    public void setRightMenuBtnVisible(boolean visible) {
        rightMenuBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTextBtnVisible(boolean visible) {
        rightTextBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setOnBackListener(OnClickListener onClickListener) {
        backBtn.setOnClickListener(onClickListener);
    }
    
    /** 设置背景为透明 */
    public void setBackgroundTransparent() {
        setBackGround(null);
    }
}
