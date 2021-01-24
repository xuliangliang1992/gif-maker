package com.xll.gif;

import android.graphics.Bitmap;

import com.highlands.common.base.BaseApplication;

import java.util.List;

/**
 * @author xuliangliang
 * @date 2021/1/7
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class MainApplication extends BaseApplication {

    public static List<Bitmap> sBitmaps;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static List<Bitmap> getBitmaps() {
        return sBitmaps;
    }

    public static void setBitmaps(List<Bitmap> bitmaps) {
        sBitmaps = bitmaps;
    }

}
