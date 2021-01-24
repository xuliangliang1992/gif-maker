package com.highlands.common.network;

/**
 * 网络类型
 *
 * @author xuliangliang
 * @date 2019-11-05
 * copyright(c) Highlands
 */
public enum NetType {

    /**
     * 表示此网络使用蜂窝传输
     */
    CELLULAR("蜂窝"),
    /**
     * 表示此网络使用Wi-Fi传输
     */
    WIFI("Wi-Fi"),
    /**
     * 表示此网络使用蓝牙传输
     */
    BLUETOOTH("蓝牙"),
    /**
     * 表示此网络使用以太网传输
     */
    ETHERNET("以太网"),
    /**
     * 表示此网络使用VPN传输
     */
    VPN("VPN"),
    /**
     * 表示此网络使用Wi-Fi感知传输
     */
    WIFI_AWARE("Wi-Fi感知"),
    /**
     * 表示此网络使用低泛传输
     */
    LOWPAN("低泛"),
    /**
     * 无网络连接
     */
    NONE("无网络");

    private String name;

    NetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
