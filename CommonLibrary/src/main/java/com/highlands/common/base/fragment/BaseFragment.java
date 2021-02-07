package com.highlands.common.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.highlands.common.base.BaseActivity;
import com.highlands.common.base.BaseView;
import com.highlands.common.base.event.EventBusUtil;
import com.highlands.common.base.event.EventMessage;
import com.highlands.common.dialog.DialogManager;
import com.highlands.common.util.ToastUtil;
import com.jakewharton.rxbinding3.view.RxView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Fragment基类
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    protected String TAG = this.getClass().getSimpleName();
    /**
     * Activity的context
     */
    protected BaseActivity mActivity;

    protected CompositeDisposable mCompositeDisposable;
    protected FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        onAttachToContext(context);
        mCompositeDisposable = new CompositeDisposable();
    }

    private void onAttachToContext(Context context) {
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mActivity);
        if (isRegisteredEventBus()) {
            EventBusUtil.register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCommonView(view);
        initView(view);
        initListener();
    }

    protected void initCommonView(View view) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisteredEventBus()) {
            EventBusUtil.unregister(this);
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }


    /**
     * 是否注册事件分发
     *
     * @return true 注册；false 不注册，默认不注册
     */
    protected boolean isRegisteredEventBus() {
        return false;
    }

    /**
     * 先注册接收事件，才能接收别人发送的 post event
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
        if (this.isResumed()) {
            onResumeReceiveEvent(event);
        }
        addDisposable(Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> onDelayReceiveEvent(event)));
    }

    /**
     * 事件消费者在事件发布之后才注册的也能接收到该事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventMessage event) {
    }

    /**
     * 当前fragment resume状态下触发事件
     *
     * @param event 事件
     */
    protected void onResumeReceiveEvent(EventMessage event) {
        Timber.tag(TAG).i("onResumeReceiveEvent");
    }

    /**
     * 延时状态下触发事件
     *
     * @param event 事件
     */
    protected void onDelayReceiveEvent(EventMessage event) {
        Timber.tag(TAG).i("onDelayReceiveEvent");
    }

    /**
     * 添加rxJava订阅
     *
     * @param disposable 订阅
     */
    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void showToast() {
        ToastUtil.showToast(mActivity, "已经到底了");
    }

    public void showToast(String msg) {
        ToastUtil.showToast(mActivity, msg);
    }

    /**
     * 显示小菊花
     */
    protected void showLoading() {
        DialogManager.getInstance().showProgressDialog(mActivity);
    }

    /**
     * 隐藏小菊花
     */
    protected void hideLoading() {
        DialogManager.getInstance().dismissProgressDialog();
    }

    protected void addClicks(View view, Consumer onNext) {
        addDisposable(RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(onNext));
    }
}
