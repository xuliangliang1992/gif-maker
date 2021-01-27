package com.xll.gif.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.base.BaseBilling2Activity;
import com.highlands.common.util.ShapeUtil;
import com.xll.gif.R;
import com.xll.gif.databinding.ActivityLaunchBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * @author xuliangliang
 * @date 2021/1/27
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class LaunchActivity extends BaseBilling2Activity {

    ActivityLaunchBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_launch);
        ShapeUtil.setShape(mBinding.tvTry, this, 10, R.color.red_DE0F04);

        mBinding.tvTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recharge();
            }
        });
    }

    @Override
    protected void consumeSuccess() {
        super.consumeSuccess();
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
    }
}
