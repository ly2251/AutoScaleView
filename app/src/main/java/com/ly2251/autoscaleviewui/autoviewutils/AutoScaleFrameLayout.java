package com.ly2251.autoscaleviewui.autoviewutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Description:
 * Created by: liuyi
 * Time: 16/4/23
 */
public class AutoScaleFrameLayout extends FrameLayout {
    public AutoScaleFrameLayout(Context context) {
        super(context);
    }

    public AutoScaleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScaleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        AutoUtils.getInstance().autoScaleViewGrop(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
