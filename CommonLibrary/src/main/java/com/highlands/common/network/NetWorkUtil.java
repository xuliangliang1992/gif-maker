package com.highlands.common.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;


/**
 * @author xuliangliang
 * @date 2019-11-05
 * copyright(c) Highlands
 */
public class NetWorkUtil {

    /**
     * @return 网络是否可用
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetWorkManager.getInstance().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            //ToDo 目前没有真机，需测试网络为Wi-Fi且Wi-Fi未登录状态是否有Internet
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            //返回所有网络信息
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return false;
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                    || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                    || networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return true;
            }
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null) {
                for (NetworkInfo info : networkInfos) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * @return 当前网络类型
     */
    @SuppressLint("MissingPermission")
    static NetType getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetWorkManager.getInstance().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NetType.NONE;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return NetType.NONE;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return NetType.CELLULAR;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return NetType.WIFI;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                return NetType.BLUETOOTH;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return NetType.ETHERNET;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                return NetType.VPN;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                return NetType.WIFI_AWARE;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                return NetType.LOWPAN;
            } else {
                return NetType.NONE;
            }
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return NetType.NONE;
            }
            int netType = networkInfo.getType();
            if (netType == ConnectivityManager.TYPE_MOBILE) {
                return NetType.CELLULAR;
            } else if (netType == ConnectivityManager.TYPE_WIFI) {
                return NetType.WIFI;
            } else if (netType == ConnectivityManager.TYPE_ETHERNET) {
                return NetType.ETHERNET;
            }
            return NetType.NONE;
        }
    }
}
