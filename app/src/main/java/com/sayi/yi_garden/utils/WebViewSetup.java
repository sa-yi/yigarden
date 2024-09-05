package com.sayi.yi_garden.utils;

import static android.content.Context.MODE_PRIVATE;
import static com.sayi.MainApplication.toast;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sayi.MainApplication;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebViewSetup {
    static LinearLayout.LayoutParams layoutParams;
    static String info = "";
    private Activity activity;
    private WebView webView;

    public static void addToClipBoard(String str, Activity activity) {
        ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData mClipData = ClipData.newPlainText("Label", str);
        manager.setPrimaryClip(mClipData);
    }


    public void setup(WebView _webView, Activity _activity) {
        webView = _webView;
        activity = _activity;
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);

        //webView.getSettings().setUserAgentString("Android/我是第四方app开发者，别打我()");
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.clearCache(true);

        webView.setWebViewClient(new DefaultWebViewClient());
        webView.setWebChromeClient(new DefaultWebChromeClient());


        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.getSettings().setBlockNetworkImage(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //允许android调用javascript
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + ";User-Agent:Android");
    }

    public class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d("progress", newProgress + "");
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMsg) {
            String errorMsg = consoleMsg.message();
            //MainApplication.toast(errorMsg,2000);
            Log.e("WebLog", errorMsg);
            return super.onConsoleMessage(consoleMsg);
        }
    }

    public class DefaultWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(
                WebView webView, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(webView, request, error);
            Log.e("WebError", error.getDescription().toString());

        }

        @Override
        public void onReceivedSslError(
                WebView arg0, SslErrorHandler handler, SslError arg2) {
            handler.proceed();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
            info = "";
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            CookieManager cookieManager = CookieManager.getInstance();
            String cookies = cookieManager.getCookie("https://bbs.66ccff.cc");
            System.out.println("Cookies: " + cookies);
            if (cookies != null)
                if (cookies.contains("wordpress_logged_in")) {
                    SharedPreferences.Editor editor = view.getContext().getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("cookies", cookies);
                    editor.apply();
                    MainApplication.toast("Cookies获取成功");
                    activity.finish();
                }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(
                WebView webView, WebResourceRequest request) {
            Uri uri = request.getUrl();

            String url = uri.toString();
            Log.d("URL", url);

            if (!url.startsWith("http")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
                Log.e("scheme", "action:" + intentUri);
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return true;
            }

            if (url.contains("bbs.66ccff.cc/?golink=")) {
                String regex = "golink=([^&]+)";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(url);

                if (matcher.find()) {
                    String golinkValue = matcher.group(1);
                    String decodedString = new String(Base64.getDecoder().decode(golinkValue));
                    Log.d("base64", decodedString);

                    if (decodedString.contains("pan.baidu.com")) {
                        WebViewSetup.addToClipBoard(decodedString, activity);
                        String package_name = "com.baidu.netdisk";
                        PackageManager packageManager = activity.getPackageManager();
                        Intent intent = packageManager.getLaunchIntentForPackage(package_name);
                        try {
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            toast("打开百度网盘失败");
                        }
                        return true;
                    }
                    String bvregex = "BV[0-9a-zA-Z]+";

                    Pattern bvpattern = Pattern.compile(bvregex);
                    Matcher bvmatcher = bvpattern.matcher(decodedString);

                    if (bvmatcher.find()) {
                        String videoId = bvmatcher.group();
                        WebViewSetup.addToClipBoard(videoId, activity);
                        String package_name = "tv.danmaku.bili";
                        PackageManager packageManager = activity.getPackageManager();
                        Intent intent = packageManager.getLaunchIntentForPackage(package_name);
                        try {
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            toast("打开哔哩哔哩失败");
                        }
                        return true;
                    }
                    if (decodedString.contains("b23.tv")) {
                        return true;
                    }
                    if (decodedString.contains("music.163.com/#/song")) {
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

}
