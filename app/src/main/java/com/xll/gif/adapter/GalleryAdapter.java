package com.xll.gif.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.highlands.common.util.glide.GlideUtil;
import com.xll.gif.R;
import com.xll.gif.databinding.ItemGalleryBinding;

import java.util.ArrayList;

/**
 * @author xuliangliang
 * @date 2021/1/12
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GalleryAdapter extends BaseSingleBindingAdapter<String, ItemGalleryBinding> {
    onGalleryClickListener mGalleryClickListener;
    SparseBooleanArray mBooleanArray;

    public GalleryAdapter() {
        mBooleanArray = new SparseBooleanArray();
        for (int i = 0; i < mItems.size(); i++) {
            mBooleanArray.put(i, false);
        }
    }

    public void setGalleryClickListener(onGalleryClickListener galleryClickListener) {
        mGalleryClickListener = galleryClickListener;
    }

    public ArrayList<Bitmap> getSelectedPhoto() {
        ArrayList<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            if (mBooleanArray.get(i)) {
                Bitmap bitmap = BitmapFactory.decodeFile(mItems.get(i));
                list.add(bitmap);
            }
        }
        return list;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_gallery;
    }

    @Override
    protected void onBindItem(ItemGalleryBinding binding, int position) {
        binding.executePendingBindings();
        if (position == 0) {
            binding.iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            binding.iv.setImageResource(R.mipmap.add);
            binding.cb.setVisibility(View.GONE);
            binding.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mGalleryClickListener != null) {
                        mGalleryClickListener.openBottomDialog();
                    }
                }
            });
        } else {
            binding.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            binding.cb.setVisibility(View.VISIBLE);
            binding.cb.setChecked(mBooleanArray.get(position));
            String picPath = mItems.get(position);
            //这里采用的是Glide为ImageVIew加载图片，很方便，这里对Glide进行了工具类的封装
            GlideUtil.loadImage2(binding.getRoot().getContext(), picPath, binding.iv);
            //            if (mGalleryClickListener != null) {
            binding.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBooleanArray.put(position, !binding.cb.isChecked());
                    notifyItemChanged(position);
                    //                        mGalleryClickListener.onImageSelect();
                }
            });
            binding.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mBooleanArray.put(position, b);
                    notifyItemChanged(position);
                }
            });
            //            }
        }
    }

    //    @Override
    //    public int getItemCount() {
    //        return mItems == null ? 0 : mItems.size() ;
    //    }

    public interface onGalleryClickListener {
        void openBottomDialog();
    }
}

