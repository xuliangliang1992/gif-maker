package com.highlands.common.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.highlands.common.BaseConstant;

import timber.log.Timber;


/**
 * @author xuliangliang
 * @date 2019-11-05
 * copyright(c) Highlands
 */
public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateReceiver";
    private NetType mNetType;
    private NetChangeObserver mNetChangeObserver;
    private long lastTime = 0;

    public NetStateReceiver() {
        mNetType = NetType.NONE;
    }

    public void setNetChangeObserver(NetChangeObserver netChangeObserver) {
        mNetChangeObserver = netChangeObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Timber.tag(TAG).e("网络监听广播异常！ ");
            return;
        }
        if (intent.getAction().equalsIgnoreCase(BaseConstant.ANDROID_NET_CHANGE_ACTION)) {
            //由于广播监听会触发多次，所以加个时间判断
            long time = System.currentTimeMillis();
            if (time - lastTime > 1000) {
                mNetType = NetWorkUtil.getNetType();
                if (NetWorkUtil.isNetworkAvailable()) {
                    Timber.tag(TAG).i("网络连接成功！ 当前网络为%s", mNetType.getName());
                    if (mNetChangeObserver != null) {
                        mNetChangeObserver.onConnect(mNetType);
                    }
                } else {
                    Timber.tag(TAG).e("网络断开！ ");
                    if (mNetChangeObserver != null) {
                        mNetChangeObserver.onDisConnect();
                    }
                }
                lastTime = time;
            }
        }
    }
}
