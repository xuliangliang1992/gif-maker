package com.highlands.common.base;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.highlands.common.base.event.EventBusUtil;
import com.highlands.common.base.event.EventMessage;
import com.highlands.common.dialog.DialogManager;
import com.highlands.common.network.NetChangeObserver;
import com.highlands.common.network.NetType;
import com.highlands.common.network.NetWorkManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Activity基类
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public abstract class BaseActivity extends AppCompatActivity implements NetChangeObserver {

    protected String TAG = this.getClass().getSimpleName();

    protected FirebaseAnalytics mFirebaseAnalytics;
    protected CompositeDisposable mCompositeDisposable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRegisteredEventBus()) {
            EventBusUtil.register(this);
        }
        mCompositeDisposable = new CompositeDisposable();
        NetWorkManager.getInstance().setNetChangeObserver(this);
        // [START shared_app_measurement]
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // [END shared_app_measurement]

        //        ImmersionBar.with(this)
//                .statusBarDarkFont(true, 0.2f)
//                .navigationBarDarkIcon(true, 0.2f)
//                .navigationBarColor(R.color.colorPrimary)
//                .keyboardEnable(true)
//                .init();

    }

    protected void initData() {

    }

    protected void initListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisteredEventBus()) {
            EventBusUtil.unregister(this);
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
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
    }

    /**
     * 接收到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventMessage event) {
    }

    /**
     * 是否监听网络变化
     *
     * @return true 监听；false 不监听，默认不监听
     */
    protected boolean isRegisteredNetChange() {
        return false;
    }

    @Override
    public void onConnect(NetType type) {
        if (isRegisteredNetChange()) {
            Timber.tag(TAG).d("onConnect%s", type.name());
        }
    }

    @Override
    public void onDisConnect() {
        if (isRegisteredNetChange()) {
            Timber.tag(TAG).d("onDisConnect");
        }
    }


    public void onLoginSuccessCallBack() {
        Timber.tag(TAG).d("登录成功返回");
    }

    /**
     * 显示小菊花
     */
    protected void showLoading() {
        DialogManager.getInstance().showProgressDialog(this);
    }

    /**
     * 隐藏小菊花
     */
    protected void hideLoading() {
        DialogManager.getInstance().dismissProgressDialog();
    }

//    protected void addCustomEvent(){
//        // [START custom_event]
//        Bundle params = new Bundle();
//        params.putString("image_name", name);
//        params.putString("full_text", text);
//        mFirebaseAnalytics.logEvent("share_image", params);
//        // [END custom_event]
//    }
}
