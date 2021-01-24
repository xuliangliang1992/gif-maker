package com.highlands.common.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.highlands.common.util.StringUtil;

/**
 * 加载富文本
 *
 * @author xuliangliang
 * @date 2020/11/15
 * copyright(c) Highlands
 */
public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        WebSettings settings = this.getSettings();
        // 设置WebView支持JavaScript
        //        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(false);
        settings.setLoadWithOverviewMode(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);  //支持放大缩小
        settings.setBuiltInZoomControls(false); //显示缩放按钮
        settings.setBlockNetworkImage(false);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(false);
        settings.setSaveFormData(false);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        //设置不让其跳转浏览器
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        this.setWebChromeClient(new WebChromeClient());
    }

    public void loadData(String data) {
        if (StringUtil.isStringNull(data)) {
            return;
        }
        this.loadData(data, "text/html", "utf-8");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

}
