package com.zarddy.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 浏览器功能
 */

public class WebViewUtils {

    public static final String MIME_TYPE = "text/html; charset=utf-8";
    public static final String ENCODING = "utf-8";

    // 初始化浏览器配置
    @SuppressLint("SetJavaScriptEnabled")
    public static void setupWebViewSettings(final Context context, final WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 不使用缓存
    }

    /**
     * 浏览器加载网页内容（针对只有页面内容，没有头尾部的页面代码）
     * @param webView 显示内容的webView
     * @param html 静态页面内容
     */
    public static void loadHtmlData(WebView webView, String html) {
        loadHtmlData(webView, html, "", "", "");
    }

    public static void loadHtmlData(WebView webView, String html, String jsFunction) {
        loadHtmlData(webView, html, jsFunction, "", "");
    }

    /**
     * 浏览器加载网页内容（针对只有页面内容，没有头尾部的页面代码）
     * @param webView 显示内容的webView
     * @param html 静态页面内容
     * @param jsFunction 需要追加到底部的js方法
     */
    public static void loadHtmlData(WebView webView, String html, String jsFunction, String cssLink, String style) {

        jsFunction = TextUtils.isEmpty(jsFunction) ? "" : jsFunction;
        cssLink = TextUtils.isEmpty(cssLink) ? "" : cssLink;
        style = TextUtils.isEmpty(style) ? "" : style;

        String body = "<html><head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + cssLink
                + "<style>"
                + "img{max-width: 100%; width: auto; height: auto;}"
                + style
                + "</style>"
                + "</head><body>"
                + html
                + "<script type=\"text/javascript\">" + jsFunction + "</script>"
                + "</body>"
                + "</html>";

        webView.loadData(body, MIME_TYPE, ENCODING);
    }

    /**
     * 获取默认UserAgent，并添加当前app版号
     */
    public static String getDefaultUserAgent(final Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
