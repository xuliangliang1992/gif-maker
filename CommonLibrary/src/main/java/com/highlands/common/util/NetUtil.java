package com.highlands.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.highlands.common.base.BaseApplication;

import androidx.annotation.NonNull;
import timber.log.Timber;

/**
 * 网络工具类
 *
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public class NetUtil {

    public static boolean checkNet() {
        Context context = BaseApplication.getInstance();
        return isWifiConnection(context) || isStationConnection(context);
    }

    public static boolean checkNetToast() {
        boolean isNet = checkNet();
        if (!isNet) {
            ToastUtil.showToast(BaseApplication.getInstance(), "网络不给力哦！");
        }
        return isNet;
    }

    /**
     * 是否使用基站联网
     *
     * @param context
     * @return
     */
    public static boolean isStationConnection(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 是否使用WIFI联网
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnection(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
        return false;
    }

    public static NetType isNetWorkState(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    Timber.v("当前WiFi连接可用 ");
                    return NetType.WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Timber.v("当前移动网络连接可用 ");
                    return NetType.NET_4G;
                }
            } else {
                Timber.v("当前没有网络连接，请确保你已经打开网络 ");
                return NetType.NO_NET;
            }
        } else {
            Timber.v("当前没有网络连接，请确保你已经打开网络 ");
            return NetType.NO_NET;
        }
        return NetType.NO_NET;
    }

    public enum NetType {
        /**
         * wifi
         */
        WIFI,
        /**
         * 移动网络
         */
        NET_4G,
        /**
         * 无网络
         */
        NO_NET
    }
}