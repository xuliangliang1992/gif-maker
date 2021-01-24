package com.highlands.common.schedulers;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * @author xll
 * @date 2018/1/1
 */

public interface BaseSchedulerProvider {

    /**
     * 切换到computation线程 用于计算任务
     *
     * @return computation线程
     */
    @NonNull
    Scheduler computation();

    /**
     * 切换到io线程
     *
     * @return io线程
     */
    @NonNull
    Scheduler io();

    /**
     * 切换到AndroidMainThread
     *
     * @return 主线程
     */
    @NonNull
    Scheduler ui();
}
