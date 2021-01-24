package com.highlands.common.view.viewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.highlands.common.util.UIUtil;

/**
 * 另一种适配方式
 *
 * @author xuliangliang
 * @date 2020/10/22
 * copyright(c) Highlands
 */
class XllRelativeLayout extends RelativeLayout {

    public XllRelativeLayout(Context context) {
        super(context);
    }

    public XllRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XllRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XllRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float scaleX = UIUtil.getInstance(getContext()).getHorValue();
        float scaleY = UIUtil.getInstance(getContext()).getVerValue();
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.width = (int) (layoutParams.width * scaleX);
            layoutParams.height = (int) (layoutParams.width * scaleY);
            layoutParams.leftMargin = (int) (layoutParams.width * scaleX);
            layoutParams.rightMargin = (int) (layoutParams.width * scaleX);
            layoutParams.topMargin = (int) (layoutParams.width * scaleY);
            layoutParams.bottomMargin = (int) (layoutParams.width * scaleY);
        }
    }
}
