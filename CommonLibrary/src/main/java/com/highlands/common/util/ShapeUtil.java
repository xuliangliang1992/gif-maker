package com.highlands.common.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

/**
 * shape操作类 可动态设置颜色圆角等 避免过多创建shape drawable
 *
 * @author xuliangliang
 * @date 2020/11/3
 * copyright(c) Highlands
 */
public class ShapeUtil {

    /**
     * @param view         view
     * @param context      context
     * @param cornerRadius 圆角 dp
     */
    public static void setShapeRadius(View view, Context context, int cornerRadius) {
        GradientDrawable mGroupDrawable = (GradientDrawable) view.getBackground();
        if (mGroupDrawable != null) {
            mGroupDrawable.mutate();
            mGroupDrawable.setCornerRadius(SystemUtil.dip2px(context, cornerRadius));
        }
    }

    /**
     * @param view       view
     * @param context    context
     * @param colorResId 颜色
     */
    public static void setShapeColor(View view, Context context, int colorResId) {
        GradientDrawable mGroupDrawable = (GradientDrawable) view.getBackground();
        if (mGroupDrawable != null) {
            mGroupDrawable.mutate();
            mGroupDrawable.setColor(ContextCompat.getColor(context, colorResId));
        }
    }

    /**
     * @param view         view
     * @param context      context
     * @param cornerRadius 圆角 dp
     * @param colorResId   颜色
     */
    public static void setShape(View view, Context context, int cornerRadius,@ColorRes int colorResId) {
        GradientDrawable mGroupDrawable = (GradientDrawable) view.getBackground();
        if (mGroupDrawable != null) {
            mGroupDrawable.mutate();
            mGroupDrawable.setCornerRadius(SystemUtil.dip2px(context, cornerRadius));
            mGroupDrawable.setColor(ContextCompat.getColor(context, colorResId));
        }
    }

    /**
     * @param view         view
     * @param context      context
     * @param cornerRadius 圆角 dp
     * @param colorResId   颜色
     * @param strokeWidth   颜色
     * @param strokeColorResId   颜色
     */
    public static void setShape(View view, Context context, int cornerRadius,@ColorRes int colorResId,int strokeWidth,@ColorRes int strokeColorResId) {
        GradientDrawable mGroupDrawable = (GradientDrawable) view.getBackground();
        if (mGroupDrawable != null) {
            mGroupDrawable.mutate();
            mGroupDrawable.setCornerRadius(SystemUtil.dip2px(context, cornerRadius));
            mGroupDrawable.setColor(ContextCompat.getColor(context, colorResId));
            mGroupDrawable.setStroke(SystemUtil.dip2px(context,strokeWidth),ContextCompat.getColor(context, strokeColorResId));
        }
    }
}
