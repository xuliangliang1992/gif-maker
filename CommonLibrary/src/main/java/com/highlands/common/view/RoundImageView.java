package com.highlands.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import com.highlands.common.util.SystemUtil;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 圆角
 *
 * @author xuliangliang
 * @date 2020/12/27
 * copyright(c) Highlands
 */
public class RoundImageView extends AppCompatImageView {

    private float width, height;
    private int radius;
    private Path mPath;

    public RoundImageView(Context context) {
        this(context, null);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        radius = SystemUtil.dip2px(context, 12);
        mPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > radius && height > radius) {

            mPath.moveTo(radius, 0);
            mPath.lineTo(width - radius, 0);
            mPath.quadTo(width, 0, width, radius);
            mPath.lineTo(width, height - radius);
            mPath.quadTo(width, height, width - radius, height);
            mPath.lineTo(radius, height);
            mPath.quadTo(0, height, 0, height - radius);
            mPath.lineTo(0, radius);
            mPath.quadTo(0, 0, radius, 0);
            canvas.clipPath(mPath);
        }

        super.onDraw(canvas);
    }
}
