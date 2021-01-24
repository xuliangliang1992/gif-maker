package com.highlands.common.base;

import androidx.databinding.ObservableArrayList;

/**
 * @author xuliangliang
 * @date 2019/9/4
 * * @param <T>
 * copyright(c) Highlands
 */
public interface BaseRefreshView<T> extends BaseRefreshContract.View {
    /**
     * 刷新数据
     *
     * @param data 数据
     */
    void refreshData(ObservableArrayList<T> data);

    /**
     * 加载更多
     *
     * @param data 数据
     */
    void loadMoreData(ObservableArrayList<T> data);
}
