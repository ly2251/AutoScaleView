package com.ly2251.autoscaleviewui.autoviewutils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Field;

/**
 * Description:
 * Created by: liuyi
 * Time: 16/4/21
 */
public class AutoUtils {
    private static final float defalutScreenWidth = 1280.0F;
    private static final float defalutScreenHeight = 720.0F;
    private static final float defalutDesity = 1.0F;

    private static float currentScreenWidth = 1920.0F;
    private static float currentScreenHeight = 1080.0F;
    private static float currentDensity = 1.5F;

    private static AutoUtils instance;

    private AutoUtils(Context context) {
        initParam(context);
    }

    /**
     * init the Utils
     *
     * @param context context
     */
    private void initParam(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        currentDensity = displayMetrics.density;

        if (displayMetrics.widthPixels <= displayMetrics.heightPixels) {
            currentScreenWidth = displayMetrics.heightPixels;
            currentScreenHeight = scaleInitHeight(displayMetrics.widthPixels);
        } else {
            currentScreenWidth = displayMetrics.widthPixels;
            currentScreenHeight = scaleInitHeight(displayMetrics.heightPixels);
        }
    }

    /**
     * init the Utils
     *
     * @param context Context
     */
    public static void init(Context context) {
        if (instance == null) {
            synchronized (AutoUtils.class) {
                if (instance == null)
                    instance = new AutoUtils(context);
            }
        }
    }

    public static AutoUtils getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("You should init AutoUtils first");
        }
        return instance;
    }

    /**
     * Scale the view group
     *
     * @param viewGroup viewGroup
     */
    public void autoScaleViewGrop(ViewGroup viewGroup) {
        if (viewGroup != null && !checkIsSame()) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                autoScaleView(viewGroup.getChildAt(i));
            }
        }
    }

    /**
     * Scale the view
     *
     * @param view view
     */
    public void autoScaleView(View view) {
        if (view == null || checkIsSame()) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            int height = layoutParams.height;
            int width = layoutParams.width;
            if (height > 0 && !checkHeightIsSame()) {
                layoutParams.height = scaleXmlHeightParamPx2CurrentPx(height);
            }

            if (width > 0 && !checkWidthIsSame()) {
                layoutParams.width = scaleXmlWidthParamPx2CurrentPx(width);
            }

            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                autoScaleMargin((ViewGroup.MarginLayoutParams) layoutParams);
            }
        }else {
            Log.e("","###!!!! layoutParams is null");
        }

        autoScaleText(view);
        autoScalePadding(view);
    }

    /**
     * Scale the px which get from the inflateXml px
     *
     * @param currentParamPx
     * @return default px
     */
    private int ScaleCurrentPxToDefaultPx(float currentParamPx) {
        int dp = (int) (currentParamPx / currentDensity + 0.5F);
        return (int) (dp * defalutDesity + 0.5F);
    }

    public int scaleXmlHeightParamPx2CurrentPx(int paramInt) {
        return Math.round(ScaleCurrentPxToDefaultPx(paramInt) * currentScreenHeight
                / defalutScreenHeight);
    }

    public int scaleXmlWidthParamPx2CurrentPx(int paramInt) {
        return Math.round(ScaleCurrentPxToDefaultPx(paramInt) * currentScreenWidth
                / defalutScreenWidth);
    }

    /**
     * auto scale margin
     *
     * @param mlp the scale view marginParams
     */
    private void autoScaleMargin(ViewGroup.MarginLayoutParams mlp) {
        if (mlp != null && !checkIsSame()) {
            if (!checkWidthIsSame()) {
                if (mlp.leftMargin != 0)
                    mlp.leftMargin = scaleXmlWidthParamPx2CurrentPx(mlp.leftMargin);
                if (mlp.rightMargin != 0)
                    mlp.rightMargin = scaleXmlWidthParamPx2CurrentPx(mlp.rightMargin);
            }

            if (!checkWidthIsSame()) {
                if (mlp.topMargin != 0)
                    mlp.topMargin = scaleXmlHeightParamPx2CurrentPx(mlp.topMargin);
                if (mlp.bottomMargin != 0)
                    mlp.bottomMargin = scaleXmlHeightParamPx2CurrentPx(mlp.bottomMargin);
            }
        }
    }

    private int scaleInitHeight(int paramInt) {
        int i = paramInt;
        if (paramInt >= 672) {
            i = paramInt;
            if (paramInt <= 720)
                i = 720;
        }
        return i;
    }

    /**
     * auto scale padding
     *
     * @param view the view need scalePadding
     */
    private void autoScalePadding(View view) {
        if (view == null || checkIsSame()) {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();

        int scalePaddingRight = paddingRight;
        int scalePaddingLeft = paddingLeft;
        int scalePaddingTop = paddingTop;
        int scalePaddingBottom = paddingBottom;

        if (!checkWidthIsSame()) {
            if (paddingLeft > 0)
                scalePaddingLeft = scaleXmlWidthParamPx2CurrentPx(paddingLeft);

            if (paddingRight > 0)
                scalePaddingRight = scaleXmlWidthParamPx2CurrentPx(paddingRight);
        }

        if (!checkHeightIsSame()) {
            if (paddingTop > 0)
                scalePaddingTop = scaleXmlHeightParamPx2CurrentPx(paddingTop);

            if (paddingBottom > 0)
                scalePaddingBottom = scaleXmlHeightParamPx2CurrentPx(paddingBottom);
        }

        view.setPadding(scalePaddingLeft, scalePaddingTop, scalePaddingRight, scalePaddingBottom);
    }

    private int getViewParam(View view, String paramString) {
        try {
            Field declaredField = View.class.getDeclaredField(paramString);
            declaredField.setAccessible(true);
            return (int) declaredField.get(view);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getTextViewParam(TextView textView, String paramString) {
        try {
            Field declaredField = TextView.class.getDeclaredField(paramString);
            declaredField.setAccessible(true);
            return (int) declaredField.get(textView);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getViewMinimumWidth(View view) {
        if (Build.VERSION.SDK_INT < 16)
            return getViewParam(view, "mMinWidth");
        return view.getMinimumWidth();
    }

    private int getViewMinimumHeight(View view) {
        if (Build.VERSION.SDK_INT < 16)
            return getViewParam(view, "mMinHeight");
        return view.getMinimumHeight();
    }

    private void autoScaleText(View view) {
        if (view != null && !checkIsSame()) {
            if (view instanceof TextView) {
                if (!checkWidthIsSame()) {
                    int width = getTextViewMinWidth((TextView) view);
                    if (width > 0)
                        ((TextView) view).setMinWidth(scaleXmlWidthParamPx2CurrentPx(width));
                }

                if (!checkHeightIsSame()) {
                    int height = getTextViewMinHeight((TextView) view);
                    if (height > 0)
                        ((TextView) view).setMinHeight(scaleXmlHeightParamPx2CurrentPx(height));
                }

                if (!checkWidthIsSame()) {
                    int width = getViewMinimumWidth(view);
                    if (width > 0)
                        view.setMinimumWidth(scaleXmlWidthParamPx2CurrentPx(width));
                }

                if (!checkHeightIsSame()) {
                    int height = getViewMinimumHeight(view);
                    if (height > 0)
                        view.setMinimumHeight(scaleXmlHeightParamPx2CurrentPx(height));
                }

                if (!checkWidthIsSame()) {
                    int text = (int) ((TextView) view).getTextSize();
                    if (text > 0) {
                        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX
                                , scaleXmlWidthParamPx2CurrentPx(text));
                    }
                }
            }
        }
    }

    private int getTextViewMinHeight(TextView view) {
        if (Build.VERSION.SDK_INT < 16)
            return getTextViewParam(view, "mMinimum");
        return view.getMinHeight();
    }

    private int getTextViewMinWidth(TextView view) {
        if (Build.VERSION.SDK_INT < 16) {
            return getTextViewParam(view, "mMinWidth");
        }
        return view.getMinimumWidth();
    }

    /**
     * check need scale
     *
     * @return
     */
    private boolean checkIsSame() {
        return checkHeightIsSame() && checkWidthIsSame();
    }

    /**
     * Scale module dp to current Px
     *
     * @param moduleDp
     * @return
     */
    public int scaleModuleDp2CurrentPx(int moduleDp) {
        return (int) ((moduleDp * defalutDesity + 0.5) * currentScreenWidth / defalutScreenWidth);
    }

    /**
     * Check width is same
     *
     * @return true is same or false
     */
    private boolean checkWidthIsSame() {
        return defalutScreenWidth / currentScreenWidth == defalutDesity / currentDensity;
    }

    public int scaleTextSize(float size) {
        if (size < 0.0F)
            return 0;
        if (checkIsSame())
            return (int) size;
        if (currentScreenWidth / defalutScreenWidth >= currentScreenHeight / defalutScreenHeight)
            return scaleXmlHeightParamPx2CurrentPx((int) size);
        return scaleXmlWidthParamPx2CurrentPx((int) size);
    }

    /**
     * Check height is same
     *
     * @return true is same or false
     */
    private boolean checkHeightIsSame() {
        return defalutScreenHeight / currentScreenHeight == defalutDesity / currentDensity;
    }
}
