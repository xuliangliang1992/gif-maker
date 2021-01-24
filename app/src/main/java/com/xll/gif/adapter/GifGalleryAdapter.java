package com.xll.gif.adapter;

import android.view.View;

import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.highlands.common.util.glide.GlideUtil;
import com.xll.gif.R;
import com.xll.gif.databinding.ItemGalleryBinding;

/**
 * @author xuliangliang
 * @date 2021/1/12
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GifGalleryAdapter extends BaseSingleBindingAdapter<String, ItemGalleryBinding> {
    onGalleryClickListener mGalleryClickListener;

    public void setGalleryClickListener(onGalleryClickListener galleryClickListener) {
        mGalleryClickListener = galleryClickListener;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_gallery;
    }

    @Override
    protected void onBindItem(ItemGalleryBinding binding, int position) {
        binding.executePendingBindings();
        binding.cb.setVisibility(View.GONE);
        String picPath = mItems.get(position);
        //这里采用的是Glide为ImageVIew加载图片，很方便，这里对Glide进行了工具类的封装
        GlideUtil.loadImage2(binding.getRoot().getContext(), picPath, binding.iv);
        if (mGalleryClickListener != null) {
            binding.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGalleryClickListener.onImageSelect(picPath);
                }
            });
        }
    }

    public interface onGalleryClickListener {
        void onImageSelect(String picPath);
    }
}

