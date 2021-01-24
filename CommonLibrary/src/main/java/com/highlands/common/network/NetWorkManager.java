package com.highlands.common.network;

import android.app.Application;
import android.content.IntentFilter;

import com.highlands.common.BaseConstant;

/**
 * @author xuliangliang
 * @date 2019-11-05
 * copyright(c) Highlands
 */
public class NetWorkManager {
    private static volatile NetWorkManager instance;
    private Application mApplication;
    private NetStateReceiver mNetStateReceiver;

    private NetWorkManager() {
        mNetStateReceiver = new NetStateReceiver();
    }

    public static NetWorkManager getInstance() {
        if (instance == null) {
            synchronized (NetWorkManager.class) {
                if (instance == null) {
                    instance = new NetWorkManager();
                }
            }
        }
        return instance;
    }

    public void setNetChangeObserver(NetChangeObserver netChangeObserver) {
        mNetStateReceiver.setNetChangeObserver(netChangeObserver);
    }

    public Application getApplication() {
        if (mApplication == null) {
            throw new RuntimeException("NetWorkManager.init() error");
        }
        return mApplication;
    }

    public void init(Application application) {
        this.mApplication = application;
        //动态广播注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseConstant.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(mNetStateReceiver, filter);
    }
}
