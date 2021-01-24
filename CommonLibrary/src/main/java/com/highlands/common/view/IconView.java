package com.highlands.common.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author xuliangliang
 * @date 2020/11/3
 * copyright(c) Highlands
 */
class IconView extends AppCompatTextView {
    public IconView(Context context) {
        super(context);
        init(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        //设置字体图标
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(font);
    }
}
