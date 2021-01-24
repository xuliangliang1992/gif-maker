package com.highlands.common.base.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * dataBinding RecyclerView Adapter
 * 单一布局
 *
 * @author xuliangliang
 * @date 2019-09-11
 * copyright(c) Highlands
 */
public abstract class BaseSingleBindingAdapter<M, B extends ViewDataBinding> extends BaseAdapter<M, BaseBindingViewHolder<B>> {

    @NotNull
    @Override
    public BaseBindingViewHolder<B> onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayout(), parent, false);
        BaseBindingViewHolder<B> viewHolder = new BaseBindingViewHolder<>(binding.getRoot());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull BaseBindingViewHolder<B> holder, int position) {
        final M m = mItems.get(position);
        if (mItemClickListener != null) {
            addDisposable(RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(unit -> mItemClickListener.onItemClick(m, position)));
        }
        if (mOnItemLongClickListener != null) {
            addDisposable(RxView.longClicks(holder.itemView)
                    .subscribe(unit -> mOnItemLongClickListener.onItemLongClick(m, position)));
        }
        onBindItem(holder.getBinding(), position);
    }

    /**
     * 布局
     *
     * @return 布局id
     */
    protected abstract @LayoutRes
    int getItemLayout();

    /**
     * 填充数据
     *
     * @param binding binding
     * @param position position
     */
    protected abstract void onBindItem(B binding, int position);

}
