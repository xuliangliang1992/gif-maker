package com.xll.gif.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.base.BaseActivity;
import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.xll.gif.MainApplication;
import com.xll.gif.R;
import com.xll.gif.databinding.AddIconActivityBinding;
import com.xll.gif.databinding.ItemIconBinding;
import com.xll.gif.view.GifAnimationDrawable;
import com.xll.gif.view.ImageUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author xuliangliang
 * @date 2021/1/21
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class AddIconActivity extends BaseActivity {
    AddIconActivityBinding mBinding;
    ObservableArrayList<Integer> arrayList;
    List<Bitmap> mList;
    GifAnimationDrawable animationDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.add_icon_activity);

        mBinding.rvList.setLayoutManager(new GridLayoutManager(this, 4));

        initData();
        initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        IconAdapter iconAdapter = new IconAdapter();
        mBinding.rvList.setAdapter(iconAdapter);
        arrayList = new ObservableArrayList<>();
        arrayList.add(R.drawable.sticker_img_0);
        arrayList.add(R.drawable.sticker_img_1);
        arrayList.add(R.drawable.sticker_img_2);
        arrayList.add(R.drawable.sticker_img_3);
        arrayList.add(R.drawable.sticker_img_4);
        arrayList.add(R.drawable.sticker_img_5);
        arrayList.add(R.drawable.sticker_img_6);
        arrayList.add(R.drawable.sticker_img_7);
        arrayList.add(R.drawable.sticker_img_8);
        arrayList.add(R.drawable.sticker_img_9);
        arrayList.add(R.drawable.sticker_img_10);
        arrayList.add(R.drawable.sticker_img_11);
        arrayList.add(R.drawable.sticker_img_12);
        arrayList.add(R.drawable.sticker_img_13);
        iconAdapter.refresh(arrayList);

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
    }

    int currPosition = -1;

    private class IconAdapter extends BaseSingleBindingAdapter<Integer, ItemIconBinding> {


        @Override
        protected int getItemLayout() {
            return R.layout.item_icon;
        }

        @Override
        protected void onBindItem(ItemIconBinding binding, int position) {
            binding.executePendingBindings();
            binding.iv.setImageResource(mItems.get(position));
            binding.cb.setChecked(currPosition == position);
            binding.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currPosition = position;
                    mBinding.mrl.addTextView(null, 200, 200, mItems.get(position) , 0, 0);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
