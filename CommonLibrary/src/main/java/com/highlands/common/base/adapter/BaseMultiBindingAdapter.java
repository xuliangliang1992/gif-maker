package com.highlands.common.base.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * dataBinding RecyclerView Adapter
 * 多类型布局
 *
 * @author xuliangliang
 * @date 2019-09-11
 * copyright(c) Highlands
 */
public abstract class BaseMultiBindingAdapter<M, B extends ViewDataBinding> extends BaseAdapter<M, BaseBindingViewHolder<B>> {

    @NotNull
    @Override
    public BaseBindingViewHolder<B> onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayout(viewType), parent, false);
        BaseBindingViewHolder<B> viewHolder = new BaseBindingViewHolder<>(binding.getRoot());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull BaseBindingViewHolder<B> holder, int position) {
        onBindItem(holder, position);
    }

    /**
     * 布局
     *
     * @param viewType 类型
     * @return 布局id
     */
    protected abstract @LayoutRes
    int getItemLayout(int viewType);

    /**
     * 填充数据
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindItem(BaseBindingViewHolder<B> holder, int position);

}
