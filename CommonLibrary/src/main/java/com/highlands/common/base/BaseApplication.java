package com.highlands.common.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.highlands.common.BuildConfig;
import com.highlands.common.network.NetWorkManager;
import com.highlands.common.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

import timber.log.Timber;

//import androidx.annotation.NonNull;
//import androidx.room.Room;
//import androidx.room.migration.Migration;
//import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * application基类 主要做一些共有的初始化动作
 *
 * @author xll
 * @date 2018/1/1
 */
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    protected static BaseApplication instance;

    public static long APP_VERSION_CODE;
    public static String APP_VERSION_NAME;

    public static String APP_PACKAGE;
    public static String APP_NAME;

    /**
     * 设备指纹
     */
    public static String FINGERPRINT;

    /**
     * 系统版本号
     */
    public static String OS_VERSION;
    /**
     * 设备名称 例如 huawei  meizu
     */
    public static String BRAND;
    /**
     * 设备型号 例如 BAH-W09  m3
     */
    public static String MODEL;
    /**
     * 序列号
     */
    public static String SERIAL;

    public static Locale defLocale;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogger();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Timber.tag(TAG).d("width = " + displayMetrics.widthPixels + "\n" + "height = " + displayMetrics.heightPixels);

        NetWorkManager.getInstance().init(this);
        //        initRoom();
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * 初始化日志
     */
    private void initLogger() {
        if (BuildConfig.LOG_DEBUG) {
            //Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
            //                    Logger.addLogAdapter(new AndroidLogAdapter());
            //                    Timber.plant(new Timber.DebugTree() {
            //                        @Override
            //                        protected void log(int priority, String tag, String message, Throwable t) {
            //                            Logger.log(priority, tag, message, t);
            //                        }
            //                    });
        }
    }

    /**
     * 初始化基础信息
     */
    protected void init() {
        final Configuration config = getApplicationContext().getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            defLocale = config.getLocales().get(0);
        } else {
            defLocale = config.locale;
        }
        Properties buildProps = new Properties();
        try {
            buildProps.load(new FileInputStream("/system/build.prop"));
        } catch (final Throwable th) {
            th.printStackTrace();
        }

        final PackageManager pm = getPackageManager();
        try {
            final PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);

            APP_NAME = getString(pi.applicationInfo.labelRes);
            Timber.tag(TAG).d("APP_NAME == " + APP_NAME);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                APP_VERSION_CODE = pi.getLongVersionCode();
            } else {
                APP_VERSION_CODE = pi.versionCode;
            }
            APP_VERSION_NAME = pi.versionName;
            APP_PACKAGE = pi.packageName;
            File extStorage = Environment.getExternalStorageDirectory();
            FINGERPRINT = Build.FINGERPRINT;
            OS_VERSION = Build.VERSION.RELEASE;
            BRAND = Build.BRAND;
            MODEL = Build.MODEL;
            SERIAL = Build.SERIAL;

            Timber.tag(TAG).d(APP_NAME + " (" + APP_PACKAGE + ")" + APP_VERSION_NAME + "(" + pi.versionCode + ")");
            Timber.tag(TAG).d("Root             dir: %s", Environment.getRootDirectory());
            Timber.tag(TAG).d("Data             dir: %s", Environment.getDataDirectory());
            Timber.tag(TAG).d("External storage dir: %s", extStorage);
            Timber.tag(TAG).d("Files            dir: %s", FileUtil.getAbsolutePath(getFilesDir()));
            Timber.tag(TAG).d("Cache            dir: %s", FileUtil.getAbsolutePath(getCacheDir()));
            Timber.tag(TAG).d("System locale       : %s", defLocale);
            Timber.tag(TAG).d("BOARD       : %s", Build.BOARD);
            Timber.tag(TAG).d("BRAND       : %s", Build.BRAND);
            Timber.tag(TAG).d("CPU_ABI     : %s", buildProps.getProperty("ro.product.cpu.abi"));
            Timber.tag(TAG).d("CPU_ABI2    : %s", buildProps.getProperty("ro.product.cpu.abi2"));
            Timber.tag(TAG).d("DEVICE      : %s", Build.DEVICE);
            Timber.tag(TAG).d("DISPLAY     : %s", Build.DISPLAY);
            Timber.tag(TAG).d("FINGERPRINT : %s", Build.FINGERPRINT);
            Timber.tag(TAG).d("ID          : %s", Build.ID);
            Timber.tag(TAG).d("MANUFACTURER: %s", buildProps.getProperty("ro.product.manufacturer"));
            Timber.tag(TAG).d("MODEL       : %s", Build.MODEL);
            Timber.tag(TAG).d("PRODUCT     : %s", Build.PRODUCT);
            Timber.tag(TAG).d("RELEASE VERSION:%s", Build.VERSION.RELEASE);
            Timber.tag(TAG).d(String.format("SERIAL%s", SERIAL));
        } catch (final PackageManager.NameNotFoundException e) {
            Timber.tag(TAG).w("init NameNotFoundException%s", e);
        }
    }

    public static boolean auth;
    public static boolean adAuth;
    public static boolean isAuth() {
        return auth;
    }

    public static void setAuth(boolean auth) {
        BaseApplication.auth = auth;
    }

    public static boolean isAdAuth() {
        return adAuth;
    }

    public static void setAdAuth(boolean adAuth) {
        BaseApplication.adAuth = adAuth;
    }
}
