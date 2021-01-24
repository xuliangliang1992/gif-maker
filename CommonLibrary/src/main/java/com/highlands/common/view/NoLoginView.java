package com.highlands.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.highlands.common.R;
import com.highlands.common.databinding.ViewNoLoginBinding;
import com.highlands.common.util.ShapeUtil;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

/**
 * 无数据View
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public class NoLoginView extends ConstraintLayout {

    private final ViewNoLoginBinding mBinding;
    private OnClickListener mOnClickListener;

    public NoLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_no_login, this, true);
        ShapeUtil.setShape(mBinding.tvLogin, context, 8, R.color.blue_3974FF);
        mBinding.tvLogin.setOnClickListener(v -> mOnClickListener.onClick(v));
    }

    public void setOnClick(OnClickListener l) {
        mOnClickListener = l;
    }
}