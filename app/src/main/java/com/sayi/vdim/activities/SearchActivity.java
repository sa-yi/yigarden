package com.sayi.vdim.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sayi.MainApplication;
import com.sayi.vdim.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.searchInput.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                String keyword = v.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    search(keyword);
                }
                return true;
            }
            return false;
        });
        binding.back.setOnClickListener(v -> finish());
    }

    private void search(String keyword) {
        if (keyword.startsWith("tid")) {
            int tid;
            String tidstr = keyword.replace("tid", "");
            if (!tidstr.isEmpty()) {
                try {
                    tid = Integer.parseInt(tidstr);
                    Intent intent = new Intent(this, PostActivity.class);
                    Uri uri = new Uri.Builder().scheme("vdim")
                            .authority("")  // authority 这里可以为空
                            .path("/viewpost")
                            .appendQueryParameter("tid", String.valueOf(tid))
                            .build();
                    intent.setData(uri);
                    startActivity(intent);
                    finish();
                    return;
                } catch (NumberFormatException e) {
                    MainApplication.toast("请输入有效tid");
                    return;
                } catch (Exception e) {
                    MainApplication.toast("意外的错误：" + e.getMessage());
                    return;
                }
            }
        }
        MainApplication.toast("搜索功能开发中...");
    }
}