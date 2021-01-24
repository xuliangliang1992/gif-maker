package com.highlands.common;

/**
 * @author xll
 * @date 2019-05-10
 */
public class BaseConstant {

    public static final int LIMIT = 10;

    public static final int LOGIN_REQUEST_CODE = 0x05;
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;
    public static final int ADD_TEXT_REQUEST_CODE = 0x03;
    public static final int ADD_ICON_REQUEST_CODE = 0x04;

    public static final String BASE_URL = "https://www.gifmakerapi.com/";
    //    public static final String BASE_URL = "https://api.prd.tianfu.icu/";
    public static final String BASE_WEIKE_URL = "http://gateway.wkinfo.com.cn/";

    public static final String SHARED_PREFERENCE_FILE_NAME = "tianFuFinance";
    public static final String DATA_BASE_NAME = "tianFuFinance.db";
    public static final String AD_MOB_KEY = "ca-app-pub-3940256099942544/5224354917";


    public static final String SUCCESS = "success";

    public static final String HTTP_ERROR = "HTTP";


    /**
     * 系统网络改变广播
     */
    public static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public final static int USER_TYPE_DOCTOR = 4;
    public final static int USER_TYPE_GAO_JI = 5;
    public final static int USER_TYPE_TEACHER = 3;


    public static final String INTENT_SEARCH_TYPE = "search_type";
    public static final String INTENT_CATEGORY_ID = "category_id";
    public static final String INTENT_LABEL_ID = "label_id";
    public static final String INTENT_CATEGORY_NAME = "category_name";
    public static final String INTENT_ID = "id";
    public static final String INTENT_TYPE = "type";
    public static final String INTENT_USER = "user_info";
    public static final String WEB_VIEW_TITLE = "web_title";
    public static final String WEB_VIEW_URL = "web_url";
    public static final String RICH_TEXT = "rich_text";
    public static final String CALL_SHOW = "call_show";


    public static final String SERVICE_IM_NUMBER = "admin";


    public static final int TYPE_INFORMATION = 1;
    public static final int TYPE_TRAIN = 2;
    public static final int TYPE_ASK = 3;
    public static final int TYPE_NONE = 0;

    public static final int TYPE_NEWS = 1;
    public static final int TYPE_POLICY = 2;
    public static final int TYPE_WEIKE = 10;


    public static final int BANNER_TYPE_POLICY = 1;
    public static final int BANNER_TYPE_VIDEO = 2;
    public static final int BANNER_TYPE_INFORMATION = 3;
    public static final int BANNER_TYPE_LIVE = 4;
    public static final int BANNER_TYPE_RICH_TEXT = 0;

    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static final int EXPIRE = -1;


    public static final int STATE_BEFORE_LIVE = 1;
    public static final int STATE_LIVING = 2;
    public static final int STATE_AFTER_LIVE = 3;
    public static final int STATE_DELAY_LIVE = 4;

    public static final int STATE_NO_PLAYBACK = 0;
    public static final int STATE_PLAYBACK_1 = 10;
    public static final int STATE_PLAYBACK_2 = 20;
    public static final int STATE_PLAYBACK_3 = 30;
    public static final int STATE_HAS_PLAYBACK = 100;
}
