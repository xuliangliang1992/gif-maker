package com.highlands.common.base;

/**
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public interface BaseRefreshContract {
    interface Presenter {
        /**
         * 刷新数据
         */
        void refreshData();

        /**
         * 加载更多
         * @param page 页码
         */
        void loadMoreData(int page);
    }

    interface View extends BaseView {
        /**
         * 是否启用下拉刷新
         *
         * @param b true 启用 默认true
         */
        void enableRefresh(boolean b);

        /**
         * 是否启用上拉加载更多
         *
         * @param b true 启用 默认true
         */
        void enableLoadMore(boolean b);

        /**
         * 刷新回调
         */
        void onRefreshEvent();

        /**
         * 加载更多的回调
         */
        void onLoadMoreEvent();

        /**
         * 自动加载的事件
         */
        void onAutoLoadEvent();

        /**
         * 停止刷新
         */
        void stopRefresh();

        /**
         * 停止加载更多
         */
        void stopLoadMore();

        /**
         * 自动加载数据
         */
        void autoLoadData();
    }
}
