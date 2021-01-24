package com.highlands.common.util.glide;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * 针对七牛存储图片url做处理
 *
 * @author xll
 * @date 2018/12/10
 */
public class MyGlideUrl extends GlideUrl {

    private String mUrl;

    public MyGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return mUrl.replace(findTokenParam(), "");
    }

    private String findTokenParam() {
        String tokenParam = "";
        int tokenKeyIndex = mUrl.indexOf("?Expires=") > 0 ? mUrl.indexOf("?Expires=") : mUrl.indexOf("?Expires=");
        if (tokenKeyIndex != -1) {
            tokenParam = mUrl.substring(tokenKeyIndex);
        }
        return tokenParam;
    }
}
