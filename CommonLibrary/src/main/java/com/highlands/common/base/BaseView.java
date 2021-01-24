package com.highlands.common.base;

import android.view.View;


/**
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public interface BaseView {

    /**
     * 设置布局
     *
     * @return 布局id
     */
    int setLayout();

    /**
     * 初始化控件
     * onViewCreated时调用
     *
     * @param view view
     */
    void initView(View view);

    /**
     * 初始化监听器
     */
    void initListener();

    /**
     * 初始化数据
     * onActivityCreated
     */
    void initData();

    /**
     * 设置presenter对象
     */
    void setPresenter();

}
