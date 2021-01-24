package com.highlands.common.base.fragment;

import android.view.View;

import com.highlands.common.base.BasePresenter;
import com.highlands.common.base.BaseRefreshView;
import com.highlands.common.view.refresh.DaisyRefreshLayout;

import androidx.databinding.ObservableArrayList;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2019-09-09
 * copyright(c) Highlands
 */
public abstract class BaseRefreshFragment<T, P extends BasePresenter> extends BaseMvpFragment<P> implements BaseRefreshView<T> {

    private DaisyRefreshLayout mRefreshLayout;
    protected int page;

    @Override
    protected void initCommonView(View view) {
        super.initCommonView(view);
        page = 1;
        mRefreshLayout = view.findViewById(onBindRefreshLayout());
        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            onRefreshEvent();
        });
        // 上拉加载
        mRefreshLayout.setOnLoadMoreListener(() -> {
            Timber.tag(TAG).d("上拉加载");
            page++;
            onLoadMoreEvent();
        });
        // 自动加载
        mRefreshLayout.setOnAutoLoadListener(() -> {
            page = 1;
            onAutoLoadEvent();
        });
    }

    /**
     * refreshLayout
     *
     * @return id
     */
    protected abstract int onBindRefreshLayout();

    /**
     * 是否启用下拉刷新
     *
     * @param b true 启用 默认true
     */
    @Override
    public void enableRefresh(boolean b) {
        mRefreshLayout.setEnableRefresh(b);
    }

    /**
     * 是否启用上拉加载更多
     *
     * @param b true 启用 默认true
     */
    @Override
    public void enableLoadMore(boolean b) {
        mRefreshLayout.setEnableLoadMore(b);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefreshEvent() {

    }

    @Override
    public void onLoadMoreEvent() {

    }

    @Override
    public void onAutoLoadEvent() {

    }

    /**
     * 停止刷新
     */
    @Override
    public void stopRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * 停止加载更多
     */
    @Override
    public void stopLoadMore() {
        mRefreshLayout.setLoadMore(false);
    }

    /**
     * 自动加载数据
     */
    @Override
    public void autoLoadData() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void hideLoading() {
        super.hideLoading();
        stopLoadMore();
        stopRefresh();
    }

    @Override
    public void refreshData(ObservableArrayList<T> data) {
        hideLoading();
    }

    @Override
    public void loadMoreData(ObservableArrayList<T> data) {
        hideLoading();
    }
}
