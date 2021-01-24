package com.highlands.common.base.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.highlands.common.R;
import com.highlands.common.databinding.ViewNoDataBinding;
import com.highlands.common.util.ObjectCastUtil;
import com.jakewharton.rxbinding3.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * dataBinding RecyclerView Adapter
 * 带空数据的单类型
 *
 * @author xuliangliang
 * @date 2019-09-11
 * copyright(c) Highlands
 */
public abstract class BaseEmptyBindingAdapter<M, B extends ViewDataBinding> extends BaseAdapter<M, BaseBindingViewHolder<B>> {

    protected static final int VIEW_TYPE_ITEM = 1;
    protected static final int VIEW_TYPE_EMPTY = 0;

    @NotNull
    @Override
    public BaseBindingViewHolder<B> onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            ViewNoDataBinding viewNoDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_no_data, parent, false);
            BaseBindingViewHolder<ViewNoDataBinding> emptyViewHolder = new BaseBindingViewHolder<>(viewNoDataBinding.getRoot());
            emptyViewHolder.setBinding(viewNoDataBinding);
            return ObjectCastUtil.cast(emptyViewHolder);
        }
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayout(), parent, false);
        BaseBindingViewHolder<B> viewHolder = new BaseBindingViewHolder<>(binding.getRoot());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NotNull BaseBindingViewHolder<B> holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
            ViewNoDataBinding binding = (ViewNoDataBinding) holder.getBinding();
            binding.imgNoData.setImageResource(getEmptyImageResource());
            binding.tvNoData.setText(getEmptyText());
        }
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
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

    }

    @Override
    public int getItemCount() {
        if (mItems.size() == 0) {
            return 1;
        }
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        if (mItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        //如果有数据，则使用ITEM的布局
        return VIEW_TYPE_ITEM;
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
     * @param binding
     * @param position
     */
    protected abstract void onBindItem(B binding, int position);

    /**
     * 资源
     *
     * @return 空数据图片
     */
    protected @DrawableRes
    int getEmptyImageResource() {
        return R.drawable.no_data;
    }


    /**
     * 空数据
     *
     * @return 文案
     */
    protected String getEmptyText() {
        return "当前无数据";
    }
}
