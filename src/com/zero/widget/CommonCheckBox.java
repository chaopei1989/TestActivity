package com.zero.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zero.App;
import com.zero.R;

/**
 * 仅CheckBox，可以通过设置android:checked来设置其选中状态<p>
 * 自定义属性：<br>
 * av_buttonStyle - 可选值有checkbox、radio、switch
 * 添加第四种style，带图标的开关 by zhanghailong-ms
 * @author chaopei
 */
public class CommonCheckBox extends ImageView implements ICustomCheckBox,
        OnClickListener {

    private static final boolean DEBUG = App.DEBUG;

    private static final String TAG = DEBUG ? "CommonCheckBox" : CommonCheckBox.class.getSimpleName();

    public static final int STYLE_CHECKBOX = 0;

    public static final int STYLE_SWICHER = 1;

    public static final int STYLE_RADIO = 2;
    
    public static final int STYLE_SWICHER_WITH_ICON = 3;

    private int mStyle = STYLE_CHECKBOX;

    boolean mChecked;
    OnCheckedChangeListener mListener;
    View mClickedView = null;
    int[] mButtonDrawables;

    public CommonCheckBox(Context context) {
        super(context);
        init();
    }

    public CommonCheckBox(Context context, int style) {
        super(context);
        mStyle = style;
        init();
    }

    private void init() {
        if (DEBUG) {
            Log.d(TAG, "[CommonCheckBox] : " + mStyle);
        }
        switch (mStyle) {
        case STYLE_SWICHER:
            mButtonDrawables = new int[] {
                    R.drawable.av_common_switch_on_enable,
                    R.drawable.av_common_switch_off_enable,
                    R.drawable.av_common_switch_on_disable,
                    R.drawable.av_common_switch_off_disable,
                    R.drawable.av_common_switch_on_enable,
                    R.drawable.av_common_switch_off_enable };
            break;
        case STYLE_SWICHER_WITH_ICON:
            mButtonDrawables = new int[] {
                    R.drawable.av_checkbox_yes,
                    R.drawable.av_checkbox_no,
                    R.drawable.av_checkbox_yes_disable,
                    R.drawable.av_checkbox_no_disable,
                    R.drawable.av_checkbox_yes,
                    R.drawable.av_checkbox_no };
            break;
        case STYLE_RADIO:
            mButtonDrawables = new int[] { R.drawable.av_common_single_choice_checked,
                    R.drawable.av_common_single_choice_unchecked,
                    R.drawable.av_common_single_choice_checked,
                    R.drawable.av_common_single_choice_unchecked,
                    R.drawable.av_common_single_choice_checked,
                    R.drawable.av_common_single_choice_unchecked };
            break;
        default:
            mButtonDrawables = new int[] { R.drawable.av_checkbox_selected,
                    R.drawable.av_checkbox_unselected,
                    R.drawable.av_checkbox_selected_disable,
                    R.drawable.av_checkbox_unselected_disable,
                    R.drawable.av_checkbox_selected,
                    R.drawable.av_checkbox_unselected };
            break;
        }

        setOnClickListener(this);
        refreshView();
    }

    public CommonCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray tArray = context.obtainStyledAttributes(attrs,
                R.styleable.av_CheckBox);
        final String style = tArray.getString(R.styleable.av_CheckBox_av_buttonStyle);
        if (DEBUG) {
            Log.d(TAG, "[CommonCheckBox] : " + style);
        }
        if (null != style) {
            if ("checkbox".equals(style)) {
                mStyle = STYLE_CHECKBOX;
            } else if ("radio".equals(style)) {
                mStyle = STYLE_RADIO;
            } else if ("switch".equals(style)){
                mStyle = STYLE_SWICHER;
            } else if("switch_with_icon".equals(style)) {
                mStyle = STYLE_SWICHER_WITH_ICON;
            } else if (DEBUG) {
                throw new RuntimeException("XML inflate error: av_buttonStyle error");
            }
        }
        tArray.recycle();
        final String checked = CommonUIUtils.getValueFromAttrs(context, attrs,
                "checked");
        if (!TextUtils.isEmpty(checked) && checked.equals("true")) {
            mChecked = true;
        }
        init();
    }

    /**
     * 0 enabled checked 1 enabled unchecked 2 unenabled checked 3 unenabled
     * unchecked 4 enabled checked pressed 5 enabled unchecked pressed
     */
    public void setButtonDrawables(int[] drawables) {
        if (drawables == null || drawables.length != mButtonDrawables.length) {
            return;
        }
        mButtonDrawables = drawables;
        refreshView();
    }

    public void setStyle(int style) {
        if (3 < style || 0 > style) {
            style = STYLE_CHECKBOX;
        }
        mStyle = style;
        init();
    }

    public void setOnCheckedChangedListener(OnCheckedChangeListener listener, View view) {
        mListener = listener;
        mClickedView = view;
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void toggle() {
        if (STYLE_RADIO == mStyle && mChecked) {
            return;
        }
        setChecked(!mChecked);
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
        refreshView();
    }

    protected void refreshView() {
        if (isEnabled()) {
            setBackgroundResource(mChecked ? mButtonDrawables[0]
                    : mButtonDrawables[1]);
        } else {
            setBackgroundResource(mChecked ? mButtonDrawables[2]
                    : mButtonDrawables[3]);
        }
    }

    public void setHalfChecked(boolean checked) {
        refreshView();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }

        mChecked = checked;
        refreshView();
        if (mListener != null) {
            mListener.onCheckedChanged(null == mClickedView ? this : mClickedView, mChecked);
        }
    }

    public void setCheckedWithoutNotifyListener(boolean checked) {
        if (mChecked == checked) {
            return;
        }

        mChecked = checked;
        refreshView();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            toggle();
        }
    }
}
