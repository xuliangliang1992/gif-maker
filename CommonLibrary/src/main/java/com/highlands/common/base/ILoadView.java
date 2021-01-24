package com.highlands.common.base;

import androidx.annotation.ColorRes;

/**
 * 加载
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public interface ILoadView {
    /**
     * 显示加载的View
     */
    void showInitLoadView();

    /**
     * 显示加载的View
     *
     * @param colorResId 背景颜色
     */
    void showInitLoadView(@ColorRes int colorResId);

    /**
     * 隐藏初始加载的View
     */
    void hideInitLoadView();


}
