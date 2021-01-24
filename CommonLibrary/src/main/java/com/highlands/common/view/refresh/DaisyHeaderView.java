package com.highlands.common.view.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
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
public class DaisyHeaderView extends RelativeLayout implements PullContract {

    private TextView mTxtLoading;
    private ObjectAnimator mRotation;

    public DaisyHeaderView(Context context) {
        this(context, null);
    }

    public DaisyHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_daisy, this);
        mTxtLoading = findViewById(R.id.txt_loading);
        mTxtLoading.setText("下拉刷新");

        ImageView imgDaisy = findViewById(R.id.img_daisy);
        mRotation = ObjectAnimator.ofFloat(imgDaisy, "rotation", 0, 360).setDuration(800);
        mRotation.setRepeatCount(ValueAnimator.INFINITE);
        mRotation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void onPullEnable(boolean enable) {
        mTxtLoading.setText(enable ? "松开刷新" : "下拉刷新");
    }

    @Override
    public void onRefresh() {
        mTxtLoading.setText("正在刷新");
        mRotation.start();
    }

    public void setRefreshing(boolean b) {
        if (b) {
            mRotation.start();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mRotation.pause();
            }
        }
    }
}
