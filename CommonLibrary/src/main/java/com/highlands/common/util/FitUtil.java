package com.highlands.common.util;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * @author xll
 * @date 2019/4/26
 */
public class FitUtil {
    /**
     * 真实屏幕的宽,不需要手动改
     */
    private static float nativeWidth = 0;

    /**
     * 在Activity的onCreate中调用,修改该Activity的density,即可完成适配,使用宽高直接使用设计图上px相等的dp值
     *
     * @param activity     需要改变的Activity
     * @param isPxEqualsDp 是否需要设置为设计图上的px直接在xml上写dp值(意思就是不需要自己计算dp值,直接写设计图上的px值,并改单位为dp),但开启后可能需要手动去设置ToolBar的大小,如果不用可以忽略
     */
    public static void autoFit(Activity activity, boolean isPxEqualsDp) {
        if (nativeWidth == 0) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            nativeWidth = point.x;
        }
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        // todo 手动设置为设计图的宽,适配将根据宽为基准,也可以设置高,但是推荐设置宽,如果不需要px=dp则不设置也行*/
        float width = 360;
        //todo 手动设置设计图的dpi,不知道可以设计图的宽除2测试一下
        int dpi = 180;
        displayMetrics.density = isPxEqualsDp ? nativeWidth / dpi / (width / dpi) : nativeWidth / dpi;
        displayMetrics.densityDpi = (int) (displayMetrics.density * 160);
    }
}
