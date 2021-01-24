package com.xll.gif.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.base.BaseActivity;
import com.xll.gif.MainApplication;
import com.xll.gif.R;
import com.xll.gif.databinding.AddTextActivityBinding;
import com.xll.gif.view.GifAnimationDrawable;
import com.xll.gif.view.ImageUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * @author xuliangliang
 * @date 2021/1/21
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class AddTextActivity extends BaseActivity {
    AddTextActivityBinding mBinding;
    List<Bitmap> mList;
    GifAnimationDrawable animationDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_text_activity);

        initData();
        initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        mList = MainApplication.getBitmaps();
        int delay = getIntent().getIntExtra("delay", 100);
        animationDrawable = new GifAnimationDrawable();
        for (int i = 0; i < mList.size(); i++) {
            Drawable drawable = new BitmapDrawable(getResources(), mList.get(i));
            animationDrawable.addFrame(drawable, delay);
        }
        mBinding.iv.setImageDrawable(animationDrawable);
        animationDrawable.start();
    }

    @Override
    protected void initListener() {
        super.initListener();
        animationDrawable.setPlayListener(new GifAnimationDrawable.onPlayListener() {
            @Override
            public void onPlay(int currPosition) {
                Bitmap bitmap = ImageUtils.createViewBitmap(mBinding.mrl, mBinding.mrl.getWidth(), mBinding.mrl.getHeight());
                mList.set(currPosition, bitmap);
            }
        });
        mBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBinding.llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication.setBitmaps(mList);

                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        mBinding.btnFinfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.mrl.addTextView(null, 200, 300, mBinding.etText.getText().toString(), Color.BLACK, 0, 0);

            }
        });
    }
}
