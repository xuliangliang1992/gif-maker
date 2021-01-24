package com.highlands.common.view;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * 贝塞尔曲线
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public class BezierTypeEvaluator implements TypeEvaluator<PointF> {
    private PointF mControlPoint;

    public BezierTypeEvaluator(PointF mControlPoint) {
        this.mControlPoint = mControlPoint;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF pointCur = new PointF();
        pointCur.x = (1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * mControlPoint.x + fraction * fraction * endValue.x;
        pointCur.y = (1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * mControlPoint.y + fraction * fraction * endValue.y;
        return pointCur;
    }
}
