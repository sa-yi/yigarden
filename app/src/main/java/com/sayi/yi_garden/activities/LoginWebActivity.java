package com.sayi.yi_garden.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sayi.MainApplication;
import com.sayi.yi_garden.utils.WebViewSetup;

public class LoginWebActivity extends AppCompatActivity {
    WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        //WebViewSetup setup = new WebViewSetup();
        //setup.setup(webView, this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );
        linearLayout.addView(webView);
        setContentView(linearLayout);
        webView.loadUrl("https://bbs.66ccff.cc");

        String ua=webView.getSettings().getUserAgentString();
        ua=WebSettings.getDefaultUserAgent(MainApplication.getContext());
        Log.d("UA",ua);
    }
}
