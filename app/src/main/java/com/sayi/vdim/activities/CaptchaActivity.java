package com.sayi.vdim.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sayi.vdim.databinding.ActivityCaptchaBinding;

public class CaptchaActivity extends AppCompatActivity {
    ActivityCaptchaBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCaptchaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btn.setOnClickListener(v->{
            binding.captcha.loadUrl("http://192.168.1.54:8080/index");
            binding.captcha.setVisibility(View.VISIBLE);
        });
        binding.captcha.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.captcha.getSettings().setBlockNetworkImage(false);
        WebSettings webSettings = binding.captcha.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //允许android调用javascript
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
    }
}
