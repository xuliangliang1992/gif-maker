package com.highlands.common.view.refresh;

/**
 * <上拉加载更多的协议><br>
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public interface PushContract {
    /**
     * 手指上滑下滑的回调
     *
     * @param enable true 达到上拉加载的距离
     */
    void onPushEnable(boolean enable);

    /**
     * 手指松开的回调
     */
    void onLoadMore();
}
