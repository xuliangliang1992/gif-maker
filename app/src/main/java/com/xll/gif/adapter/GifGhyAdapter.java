package com.xll.gif.adapter;

import android.view.View;

import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.highlands.common.schedulers.SchedulerProvider;
import com.highlands.common.util.StringUtil;
import com.highlands.common.util.glide.GlideUtil;
import com.jakewharton.rxbinding3.view.RxView;
import com.xll.gif.R;
import com.xll.gif.databinding.ItemGalleryBinding;
import com.xll.gif.fragment.GifListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * @author xuliangliang
 * @date 2021/1/12
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GifGhyAdapter extends BaseSingleBindingAdapter<GifListFragment.GifBean, ItemGalleryBinding> {
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
        String url = mItems.get(position).getUrl();
        String picPath = mItems.get(position).getPath();
        if (StringUtil.isStringNull(picPath)) {
            binding.ivDownload.setVisibility(View.VISIBLE);
            binding.ivShare.setVisibility(View.GONE);
        } else {
            binding.ivDownload.setVisibility(View.GONE);
            binding.ivShare.setVisibility(View.VISIBLE);
        }
        //这里采用的是Glide为ImageVIew加载图片，很方便，这里对Glide进行了工具类的封装
        GlideUtil.loadImage(binding.getRoot().getContext(), url, binding.iv, 10);
        if (mGalleryClickListener != null) {
            addDisposable(RxView.clicks(binding.ivDownload)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) throws Exception {
                            mGalleryClickListener.onImageDownload(position,url, mItems.get(position));
                        }
                    }));
            addDisposable(RxView.clicks(binding.ivShare)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .observeOn(SchedulerProvider.getInstance().ui())
                    .subscribe(new Consumer<Unit>() {
                        @Override
                        public void accept(Unit unit) throws Exception {
                            mGalleryClickListener.onImageShare(position, picPath);
                        }
                    }));
        }
    }

    public interface onGalleryClickListener {
        void onImageDownload(int position, String url, GifListFragment.GifBean gifBean);

        void onImageShare(int position, String path);
    }
}

