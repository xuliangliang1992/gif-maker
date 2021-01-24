package com.highlands.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.highlands.common.R;
import com.highlands.common.databinding.ViewNoDataBinding;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

/**
 * 无数据View
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public class NoDataView extends RelativeLayout {

    private final ViewNoDataBinding mBinding;

    public NoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_no_data, this, true);
    }

    public void setNoDataBackground(@ColorRes int colorResId) {
        mBinding.rlNoDataRoot.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
    }

    public void setNoDataView(@DrawableRes int imgResId) {
        mBinding.imgNoData.setImageResource(imgResId);
    }
}