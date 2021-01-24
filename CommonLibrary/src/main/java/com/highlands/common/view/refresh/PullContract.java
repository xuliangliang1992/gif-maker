package com.highlands.common.view.refresh;

/**
 * <下拉刷新的协议><br>
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public interface PullContract {
    /**
     * 手指上滑下滑的回调
     *
     * @param enable true 达到下拉刷新的距离
     */
    void onPullEnable(boolean enable);

    /**
     * 手指松开的回调
     */
    void onRefresh();

}
