package com.highlands.common.base;

import com.highlands.common.schedulers.BaseSchedulerProvider;
import com.highlands.common.schedulers.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author xuliangliang
 * @date 2019-09-11
 * copyright(c) Highlands
 */
public abstract class BasePresenter<V extends BaseView> implements IBasePresenter {
    protected CompositeDisposable mCompositeDisposable;
    protected BaseSchedulerProvider mSchedulerProvider;
    protected V mView;
    protected String TAG = this.getClass().getSimpleName();

    public BasePresenter(V view) {
        mView = view;
    }

    /**
     * 订阅
     */
    @Override
    public void subscriber() {
        mCompositeDisposable = new CompositeDisposable();
        mSchedulerProvider = SchedulerProvider.getInstance();
    }

    /**
     * 取消订阅
     */
    @Override
    public void unSubscriber() {
        mCompositeDisposable.clear();
    }
}
