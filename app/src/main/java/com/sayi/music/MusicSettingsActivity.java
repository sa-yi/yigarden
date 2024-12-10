package com.sayi.music;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sayi.vdim.R;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.utils.DarkModeUtils;

public class MusicSettingsActivity extends AppCompatActivity {
    ActivityMusicSettingsBinding binding;
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
        binding=ActivityMusicSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
