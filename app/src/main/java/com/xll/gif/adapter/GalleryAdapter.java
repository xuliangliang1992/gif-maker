package com.xll.gif.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.View;

import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.highlands.common.util.glide.GlideUtil;
import com.xll.gif.R;
import com.xll.gif.databinding.ItemGalleryBinding;

import java.io.File;
import java.util.ArrayList;

/**
 * @author xuliangliang
 * @date 2021/1/12
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GalleryAdapter extends BaseSingleBindingAdapter<String, ItemGalleryBinding> {
    File mImgDir;
    onGalleryClickListener mGalleryClickListener;
    SparseBooleanArray mBooleanArray;

    public void setImgDir(File imgDir) {
        mImgDir = imgDir;
        mBooleanArray = new SparseBooleanArray();
        for (int i = 0; i < mItems.size(); i++) {
            mBooleanArray.put(i, false);
        }
        notifyDataSetChanged();
    }

    public void setGalleryClickListener(onGalleryClickListener galleryClickListener) {
        mGalleryClickListener = galleryClickListener;
    }

    public ArrayList<Bitmap> getSelectedPhoto() {
        ArrayList<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < mBooleanArray.size(); i++) {
            if (mBooleanArray.get(i)) {
                Bitmap bitmap= BitmapFactory.decodeFile(mImgDir.getAbsolutePath() + "/" + mItems.get(i));
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
        if (mImgDir != null) {
            binding.cb.setChecked(mBooleanArray.get(position));
            String picPath = mImgDir.getAbsolutePath() + "/" + mItems.get(position);
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
            //            }
        }
    }

    interface onGalleryClickListener {
        void onImageSelect();
    }
}

