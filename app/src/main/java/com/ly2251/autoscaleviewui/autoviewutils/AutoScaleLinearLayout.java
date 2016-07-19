package com.ly2251.autoscaleviewui.autoviewutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Description:
 * Created by: liuYi
 * Time: 16/4/23
 */
public class AutoScaleLinearLayout extends LinearLayout {
    public AutoScaleLinearLayout(Context context) {
        super(context);
    }

    public AutoScaleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScaleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        AutoUtils.getInstance().autoScaleViewGroup(this);
    }
}
