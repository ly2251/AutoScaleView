package com.ly2251.autoscaleviewui.autoviewutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Description:
 * Created by: liuYi
 * Time: 16/4/23
 */
public class AutoScaleRelativeLayout extends RelativeLayout {
    public AutoScaleRelativeLayout(Context context) {
        super(context);
    }

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScaleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        AutoUtils.getInstance().autoScaleViewGroup(this);
    }
}
