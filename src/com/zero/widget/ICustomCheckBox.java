/**
 *
 */
package com.zero.widget;

import android.view.View;
import android.widget.Checkable;

/**
 * 实现该接口，表示已经具备了与“选择框”相关的功能
 *
 * @author Jiongxuan Zhang
 *
 */
public interface ICustomCheckBox extends Checkable {

    /**
     * 设置“选择框”开关改变时触发的事件
     *
     * @param listener 你懂的
     */
    void setOnCheckedChangeListener(OnCheckedChangeListener listener);


    /**
     * 当“选择框”的开关被改变时，触发该事件
     */
    public interface OnCheckedChangeListener {

        /**
         * 选择框的开关被改变
         * @param v 选择框的View
         * @param isChecked 改变后的开关值
         */
        public void onCheckedChanged(View v, boolean isChecked);
    }
}
