package com.highlands.common.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author xuliangliang
 * @date 2020/10/22
 * copyright(c) Highlands
 */
public class UIUtil {
    private Context mContext;

    // 标准值
    private static final float STANDARD_WIDTH = 1080F;
    private static final float STANDARD_HEIGHT = 1920F;

    //实际值
    private static float displayMetricsWidth;
    private static float displayMetricsHeight;

    private static UIUtil instance;

    public static UIUtil getInstance(Context context) {
        if (instance == null) {
            instance = new UIUtil(context);
        }
        return instance;
    }

    private UIUtil(Context context) {
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (displayMetrics.widthPixels == 0.0f || displayMetrics.heightPixels == 0.0f) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int systemBarHeight = getSystemBarHeight(context);
            if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                //横屏
                displayMetricsWidth = displayMetricsHeight;
                displayMetricsHeight = displayMetrics.widthPixels - systemBarHeight;
            } else {
                //竖屏
                displayMetricsWidth = displayMetrics.widthPixels;
                displayMetricsHeight = displayMetrics.heightPixels - systemBarHeight;

            }
        }
    }

    private static final String DIME_CLASS = "com.android.internal.R$dimen";

    private int getSystemBarHeight(Context context) {
        return getValue(context, DIME_CLASS, "system_bar_hight", 48);
    }

    private int getValue(Context context, String dimenClass, String systemBarHeight, int i) {
        try {
            Class<?> clz = Class.forName(dimenClass);
            Object obj = clz.newInstance();
            Field field = clz.getField(systemBarHeight);
            int id = Integer.parseInt(field.get(obj).toString());
            return context.getResources()
                    .getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public float getHorValue() {
        return displayMetricsWidth / STANDARD_WIDTH;
    }

    public float getVerValue() {
        return displayMetricsHeight / (STANDARD_HEIGHT - getSystemBarHeight(mContext));
    }
}
