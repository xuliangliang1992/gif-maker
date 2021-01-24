package com.highlands.common.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public abstract class BaseRefreshLayout extends SuperSwipeRefreshLayout {
    /**
     * 是否启用下拉刷新
     */
    private boolean isEnableRefresh = true;
    /**
     * 是否启用上拉加载更多
     */
    private boolean isEnableLoadMore = true;
    /**
     * 下拉刷新监听器
     */
    protected OnRefreshListener mOnRefreshListener;
    /**
     * 上拉加载更多监听器
     */
    protected OnLoadMoreListener mOnLoadMoreListener;
    /**
     * 自动加载的回调
     */
    protected OnAutoLoadListener mOnAutoLoadListener;

    public interface OnRefreshListener {
        /**
         * 下拉刷新
         */
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        /**
         * 上啦加载
         */
        void onLoadMore();
    }

    public interface OnAutoLoadListener {
        /**
         * 自动刷新
         */
        void onAutoLoad();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setOnAutoLoadListener(OnAutoLoadListener onAutoLoadListener) {
        mOnAutoLoadListener = onAutoLoadListener;
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 是否启用下拉刷新
     *
     * @param enableRefresh true 可以下拉
     */
    public void setEnableRefresh(boolean enableRefresh) {
        isEnableRefresh = enableRefresh;
    }

    /**
     * 是否启用加载更多
     *
     * @param enableLoadMore true 可以上拉
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        isEnableLoadMore = enableLoadMore;
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        postDelayed(() -> {
            showRefresh();
            setRefreshing(true);
            if (mOnAutoLoadListener != null) {
                mOnAutoLoadListener.onAutoLoad();
            }
        }, 1000);
    }

    /**
     * 上拉 如果禁用了加载更多则就直接返回了
     *
     * @param ev     触摸事件
     * @param action 事件类型
     * @return false 禁用
     */
    @Override
    protected boolean handlerPushTouchEvent(MotionEvent ev, int action) {
        if (!isEnableLoadMore) {
            return false;
        }
        return super.handlerPushTouchEvent(ev, action);
    }

    /**
     * 下拉 如果禁用了下拉刷新就直接返回了
     *
     * @param ev     触摸事件
     * @param action 事件类型
     * @return false 禁用
     */
    @Override
    protected boolean handlerPullTouchEvent(MotionEvent ev, int action) {
        if (!isEnableRefresh) {
            return false;
        }
        return super.handlerPullTouchEvent(ev, action);
    }

    /**
     * 刷新
     */
    public abstract void showRefresh();
}
