package com.xll.gif.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.base.BaseActivity;
import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.highlands.common.util.ShapeUtil;
import com.xll.gif.MainApplication;
import com.xll.gif.R;
import com.xll.gif.databinding.AddTextActivityBinding;
import com.xll.gif.databinding.ItemColorBinding;
import com.xll.gif.view.GifAnimationDrawable;
import com.xll.gif.view.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author xuliangliang
 * @date 2021/1/21
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class AddTextActivity extends BaseActivity {
    AddTextActivityBinding mBinding;
    List<Bitmap> mList;
    GifAnimationDrawable animationDrawable;
    private int color;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_text_activity);
        mBinding.rvList.setLayoutManager(new GridLayoutManager(this, 5));

        initData();
        initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);
        colors.add(Color.RED);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.rgb(110,110,110));
        colors.add(Color.rgb(210,210,210));
        colors.add(Color.rgb(10,10,10));
        colors.add(Color.rgb(60,80,100));
        ColorAdapter adapter = new ColorAdapter();
        mBinding.rvList.setAdapter(adapter);
        adapter.refresh(colors);

        mList = MainApplication.getBitmaps();
        int delay = getIntent().getIntExtra("delay", 100);
        animationDrawable = new GifAnimationDrawable();
        for (int i = 0; i < mList.size(); i++) {
            Drawable drawable = new BitmapDrawable(getResources(), mList.get(i));
            animationDrawable.addFrame(drawable, delay);
        }
        animationDrawable.setOneShot(false);
        mBinding.iv.setImageDrawable(animationDrawable);
        animationDrawable.setDuration(delay);
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

        mBinding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textSize = mBinding.etSize.getText().toString();

                mBinding.mrl.addTextView(null, 200, 200, mBinding.etText.getText().toString(), color == 0 ? Color.BLACK : color, Integer.parseInt(textSize), 0);
            }
        });
    }

    class ColorAdapter extends BaseSingleBindingAdapter<Integer, ItemColorBinding> {

        @Override
        protected int getItemLayout() {
            return R.layout.item_color;
        }

        @Override
        protected void onBindItem(ItemColorBinding binding, int position) {
            binding.executePendingBindings();
            ShapeUtil.setShape2(binding.tvColor, binding.getRoot().getContext(),
                    20, mItems.get(position));
            binding.tvColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    color = mItems.get(position);
                }
            });
        }
    }
}
