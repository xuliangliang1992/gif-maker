package com.highlands.common.view.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highlands.common.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public class DaisyFooterView extends RelativeLayout implements PushContract {

    private TextView mTxtLoading;
    private ObjectAnimator mRotation;

    public DaisyFooterView(Context context) {
        this(context, null);
    }

    public DaisyFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_daisy, this);
        mTxtLoading = findViewById(R.id.txt_loading);
        mTxtLoading.setText("上拉加载更多...");
        ImageView imgDaisy = findViewById(R.id.img_daisy);

        mRotation = ObjectAnimator.ofFloat(imgDaisy, "rotation", 0, 360).setDuration(800);
        mRotation.setRepeatCount(ValueAnimator.INFINITE);
        mRotation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void onPushEnable(boolean enable) {
        mTxtLoading.setText(enable ? "松开加载" : "上拉加载");
    }

    @Override
    public void onLoadMore() {
        mTxtLoading.setText("正在加载...");
        mRotation.start();
    }

    public void setLoadMore(boolean b) {
        if (b) {
            mRotation.start();
        } else {
            mRotation.pause();
        }
    }
}
