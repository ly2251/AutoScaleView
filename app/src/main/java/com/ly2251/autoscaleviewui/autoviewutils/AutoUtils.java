package com.ly2251.autoscaleviewui.autoviewutils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import java.lang.reflect.Field;

/**
 * Description:
 * Created by: liuYi
 * Time: 16/4/21
 */
public class AutoUtils {
    private static final float defalutWidth = 1280.0F;
    private static final float defalutHeight = 720.0F;
    private static final float defalutDensity = 1.0F;

    private static float currentWidth = 1920.0F;
    private static float currentHeight = 1080.0F;
    private static float currentDensity = 1.5f;

    private static AutoUtils instance;

    private AutoUtils(Context context) {
        initParam(context);
    }

    /**
     * init the Utils and get the current info
     *
     * @param context
     */
    private void initParam(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        /**
         * 如何是盒子设备，请打开此处注释代码
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            wm.getDefaultDisplay().getRealMetrics(localDisplayMetrics);
//        } else {
            wm.getDefaultDisplay().getMetrics(localDisplayMetrics);
//        }
        currentDensity = localDisplayMetrics.density;

        if (localDisplayMetrics.widthPixels <= localDisplayMetrics.heightPixels) {
            currentWidth = localDisplayMetrics.heightPixels;
            currentHeight = localDisplayMetrics.widthPixels;
        } else {
            currentWidth = localDisplayMetrics.widthPixels;
            currentHeight = localDisplayMetrics.heightPixels;
        }
    }

    /**
     * need init first
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
            throw new IllegalArgumentException("Not initialized!");
        }
        return instance;
    }

    public void autoScaleViewGroup(ViewGroup viewGroup) {
        if (viewGroup != null && !checkIsSame()) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                autoScaleView(viewGroup.getChildAt(i));
            }
        }
    }

    private void autoScaleView(View view) {
        if (view == null || checkIsSame()) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            int height = layoutParams.height;
            int width = layoutParams.width;
            if (height > 0 && !checkHeightIsSame()) {
                layoutParams.height = scaleHeightParamFixPx2PxInt(height);
            }

            if (width > 0 && !checkWidthIsSame()) {
                layoutParams.width = scaleWidthParamFixPx2PxInt(width);
            }

            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                autoScaleMargin((ViewGroup.MarginLayoutParams) layoutParams);
            }
        }
        autoScaleText(view);
        autoScalePadding(view);
    }

    private void autoScaleText(View view) {
        if (view != null && !checkIsSame()) {
            if (view instanceof TextView) {
                if (!checkWidthIsSame()) {
                    int width = getTextViewMinWidth((TextView) view);
                    if (width > 0)
                        ((TextView) view).setMinWidth(scaleWidthParamFixPx2PxInt(width));
                }

                if (!checkHeightIsSame()) {
                    int height = getTextViewMinHeight((TextView) view);
                    if (height > 0)
                        ((TextView) view).setMinHeight(scaleHeightParamFixPx2PxInt(height));
                }

                if (!checkWidthIsSame()) {
                    int width = getViewMinimumWidth(view);
                    if (width > 0)
                        view.setMinimumWidth(scaleWidthParamFixPx2PxInt(width));
                }

                if (!checkHeightIsSame()) {
                    int height = getViewMinimumHeight(view);
                    if (height > 0)
                        view.setMinimumHeight(scaleHeightParamFixPx2PxInt(height));
                }

                if (!checkWidthIsSame()) {
                    int size = (int) ((TextView) view).getTextSize();
                    if (size > 0) {
                        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                scaleWidthParamFixPx2PxInt(size));
                    }
                }

                if (!checkHeightIsSame()) {
                    int height = getTextViewLineSpace((TextView) view);
                    int multiplier = getTextViewLineMultiplier((TextView) view);
                    if (height > 0) {
                        ((TextView) view).setLineSpacing(scaleHeightParamFixPx2PxInt(height), multiplier);
                    }
                }
            }
        }
    }

    private int getViewMinimumHeight(View view) {
        if (Build.VERSION.SDK_INT < 16)
            return getViewParam(view, "mMinHeight");
        return view.getMinimumHeight();
    }

    private int getViewMinimumWidth(View view) {
        if (Build.VERSION.SDK_INT < 16) {
            return getViewParam(view, "mMinWidth");
        }
        return view.getMinimumWidth();
    }

    private int getTextViewMinHeight(TextView textView) {
        if (Build.VERSION.SDK_INT < 16) {
            return getTextViewParam(textView, "mMinimum");
        }
        return textView.getMinHeight();
    }

    private int getTextViewMinWidth(TextView textView) {
        if (Build.VERSION.SDK_INT < 16) {
            return getTextViewParam(textView, "mMinWidth");
        }
        return textView.getMinimumWidth();
    }

    private int getTextViewLineSpace(TextView textView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getTextViewParam(textView, "mSpacingAdd");
        }
        return (int) textView.getLineSpacingExtra();
    }

    private int getTextViewLineMultiplier(TextView textView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getTextViewParam(textView, "mSpacingMult");
        }
        return (int) textView.getLineSpacingMultiplier();
    }

    private int getViewParam(View view, String paramString) {
        try {
            Field declaredField = View.class.getDeclaredField(paramString);
            declaredField.setAccessible(true);
            return (Integer) declaredField.get(view);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getTextViewParam(TextView textView, String paramString) {
        try {
            Field declaredField = TextView.class.getDeclaredField(paramString);
            declaredField.setAccessible(true);
            return (Integer) declaredField.get(textView);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void autoScaleMargin(ViewGroup.MarginLayoutParams mlp) {
        if (mlp != null && !checkIsSame()) {
            if (!checkWidthIsSame()) {
                if (mlp.leftMargin != 0)
                    mlp.leftMargin = scaleWidthParamFixPx2PxInt(mlp.leftMargin);
                if (mlp.rightMargin != 0)
                    mlp.rightMargin = scaleWidthParamFixPx2PxInt(mlp.rightMargin);
            }

            if (!checkHeightIsSame()) {
                if (mlp.topMargin != 0)
                    mlp.topMargin = scaleHeightParamFixPx2PxInt(mlp.topMargin);
                if (mlp.bottomMargin != 0)
                    mlp.bottomMargin = scaleHeightParamFixPx2PxInt(mlp.bottomMargin);
            }
        }
    }

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
                scalePaddingLeft = scaleWidthParamFixPx2PxInt(paddingLeft);

            if (paddingRight > 0)
                scalePaddingRight = scaleWidthParamFixPx2PxInt(paddingRight);
        }

        if (!checkHeightIsSame()) {
            if (paddingTop > 0)
                scalePaddingTop = scaleHeightParamFixPx2PxInt(paddingTop);

            if (paddingBottom > 0)
                scalePaddingBottom = scaleHeightParamFixPx2PxInt(paddingBottom);
        }

        view.setPadding(scalePaddingLeft, scalePaddingTop, scalePaddingRight, scalePaddingBottom);
    }

    private int scaleHeightParamFixPx2PxInt(int paramInt) {
        return Math.round(ScaleCurrentPxToDefaultPx(paramInt) * currentHeight / defalutHeight);
    }

    private int scaleWidthParamFixPx2PxInt(int paramInt) {
        return Math.round(ScaleCurrentPxToDefaultPx(paramInt) * currentWidth / defalutWidth);
    }

    private int ScaleCurrentPxToDefaultPx(float paramFloat) {
        int dp = (int) (paramFloat / currentDensity + 0.5f);
        return (int) (dp * defalutDensity + 0.5f);
    }

    private boolean checkWidthIsSame() {
        return defalutWidth / currentWidth == defalutDensity / currentDensity;
    }

    private boolean checkHeightIsSame() {
        return defalutHeight / currentHeight == defalutDensity / currentDensity;
    }

    private boolean checkIsSame() {
        return checkHeightIsSame() && checkWidthIsSame();
    }

    public int scaleTextSize(float size) {
        if (size < 0.0f) {
            return 0;
        }
        if (checkIsSame())
            return (int) size;
        if (currentWidth / defalutWidth >= currentHeight / defalutHeight)
            return scaleHeightParamFixPx2PxInt((int) size);
        return scaleWidthParamFixPx2PxInt((int) size);
    }

    /**
     * Scale module vertical dp to current px
     *
     * @param moduleDp the dp in Module
     * @return current Px
     */
    public int scaleModuleHorizontalDp2Px(int moduleDp) {
        return (int) (moduleDp / (defalutWidth / defalutDensity + 0.5f) * (currentWidth / currentDensity + 0.5) * currentDensity + 0.5);
    }

    /**
     * Scale module vertical dp to current px
     *
     * @param moduleDp the dp in Module
     * @return current Px
     */
    public int scaleModuleVerticalDp2Px(int moduleDp) {
        return (int) (moduleDp / (defalutHeight / defalutDensity + 0.5f) * (currentHeight / currentDensity + 0.5) * currentDensity + 0.5);
    }

    /**
     * Scale module vertical px to current px
     *
     * @param modulePx the px in module
     * @return current px
     */
    public int scaleModuleVerticalPx2Px(int modulePx) {
        return (int) ((modulePx / defalutDensity + 0.5f) / (defalutHeight / defalutDensity + 0.5f) * (currentHeight / currentDensity + 0.5) * currentDensity + 0.5);
    }

    /**
     * Scale module horizontal px to current px
     *
     * @param modulePx the px in module
     * @return current px
     */
    public int scaleModuleHorizontalPx2Px(int modulePx) {
        return (int) ((modulePx / defalutDensity + 0.5f) / (defalutWidth / defalutDensity + 0.5f) * (currentWidth / currentDensity + 0.5) * currentDensity + 0.5);
    }

    public float getCurrentWidth() {
        return currentWidth;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

    public float getCurrentDensity() {
        return currentDensity;
    }
}
