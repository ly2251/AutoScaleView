package com.ly2251.autoscaleviewui.autoviewutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Description:
 * Created by: liuyi
 * Time: 16/4/23
 */
public class AutoTextView extends TextView {
    public AutoTextView(Context context) {
        this(context,null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextSize(getTextSize());
    }

    public void setTextSize(final float n){
        this.setTextSize(0,n);
    }

    public void setTextSize(final int n, final float n2){
        super.setTextSize(n,AutoUtils.getInstance().scaleTextSize(n2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
