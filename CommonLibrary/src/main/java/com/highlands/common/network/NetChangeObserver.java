package com.highlands.common.network;


/**
 * @author xuliangliang
 * @date 2019-11-05
 * copyright(c) Highlands
 */
public interface NetChangeObserver {

    /**
     * 网络连接且可用
     *
     * @param type 网络类型
     */
    void onConnect(NetType type);

    /**
     * 网络断开或网络不可用
     */
    void onDisConnect();
}
