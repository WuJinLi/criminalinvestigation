package com.ci.criminalinvestigation;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ci.criminalinvestigation.base.BaseActivity;

/**
 * @author: wjl
 * @date:2018/5/30
 * @desc 定位详情信息展示
 */

public class LocationInfoActivity extends BaseActivity {
    public WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_location_info);
        initData();
        initViews();
    }

    private void initData() {

    }

    private void initViews() {
        webView = findViewById(R.id.wv_web);
        webView.requestFocusFromTouch();
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//解决图片不显示的问题
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);// 设置可以支持缩放
        webView.getSettings().setBuiltInZoomControls(false);// 设置出现缩放工具
        webView.getSettings().setUseWideViewPort(true);//扩大比例的缩放
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出窗口
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " " +
                "DLB/DLB");
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);


        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading("加载中");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                cancleLoading();
            }
        });
    }

}
