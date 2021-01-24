package com.highlands.common.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.highlands.common.R;
import com.highlands.common.databinding.ViewLoadingBinding;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

/**
 * 加载中View
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public class LoadingView extends RelativeLayout {
    private final AnimationDrawable mAnimationDrawable;
    private ViewLoadingBinding binding;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_loading, this, true);
        mAnimationDrawable = (AnimationDrawable) binding.imgLoading.getDrawable();
    }

    public void setLoadingBackgroundColor(@ColorRes int colorResId) {
        binding.rlLoadingRoot.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
    }

    private void startLoading() {
        mAnimationDrawable.start();
    }

    private void stopLoading() {
        mAnimationDrawable.stop();
    }

    public void loading(boolean b) {
        if (b) {
            startLoading();
        } else {
            stopLoading();
        }
    }
}
