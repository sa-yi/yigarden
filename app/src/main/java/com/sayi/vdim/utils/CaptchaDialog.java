package com.sayi.vdim.utils;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CaptchaDialog extends Dialog {

    public CaptchaDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        WebView webView = new WebView(getContext());
        webView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.addView(webView);
        setContentView(linearLayout);
        setup(webView);
        // 加载包含人机验证的网页
        webView.loadUrl("https://captcha.sa-yi.cn/indey");

        setOnCancelListener(dialog -> {

        });
        setOnDismissListener(dialog -> {

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    // 提供方法供外部调用，例如验证结果处理
    public void onCaptchaSuccess(String result) {
        // 处理验证成功的结果
    }
    public void setup(WebView webView) {
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new DefaultWebViewClient());


        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //允许android调用javascript
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.addJavascriptInterface(
            new Object() {
                @JavascriptInterface
                public void success(String token){
                    Toast.makeText(getContext(),token,Toast.LENGTH_LONG).show();
                    // 获取ClipboardManager对象
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建ClipData对象
                    ClipData clip = ClipData.newPlainText("token", token);
                    // 将ClipData对象设置到ClipboardManager
                    clipboard.setPrimaryClip(clip);
                    // 显示Toast消息，告知用户文本已复制
                    Toast.makeText(getContext(), "文本已复制", Toast.LENGTH_SHORT).show();
                }
            },
            "app");
    }


    public static class DefaultWebViewClient extends WebViewClient {
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
    }
}
