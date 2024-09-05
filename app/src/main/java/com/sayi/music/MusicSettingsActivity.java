package com.sayi.music;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sayi.yi_garden.R;
import com.sayi.yi_garden.utils.DarkModeUtils;

public class MusicSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (DarkModeUtils.isDarkMode(this)) {
            //黑色状态栏文本
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_music_settings);
    }
}
