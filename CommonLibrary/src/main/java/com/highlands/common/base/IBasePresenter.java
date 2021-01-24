package com.highlands.common.base;


/**
 * presenter 基类
 *
 * @author xll
 * @date 2018/1/1
 */
public interface IBasePresenter {
    /**
     * 订阅
     */
    void subscriber();

    /**
     * 取消订阅
     */
    void unSubscriber();
}
