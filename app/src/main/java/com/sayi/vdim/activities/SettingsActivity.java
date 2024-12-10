package com.sayi.vdim.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.sayi.MainApplication;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.ActivitySettingsBinding;
import com.sayi.vdim.utils.DarkModeUtils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("theme", false);
        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
            MainApplication.toast(key);
            if (Objects.equals(key, "theme")) {
                boolean ifDark = sharedPreferences1.getBoolean("theme", false);
                if (ifDark) {
                    DarkModeUtils.applyNightMode(SettingsActivity.this);
                } else {
                    DarkModeUtils.applyDayMode(SettingsActivity.this);
                }
            }
        });

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);// 获取 activity preference
            Preference activityPreference = findPreference("system_settings");

            if (activityPreference != null) {
                // 设置点击监听器
                activityPreference.setOnPreferenceClickListener(preference -> {
                    // 跳转到应用设置界面
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + requireActivity().getPackageName()));
                    startActivity(intent);
                    return true;
                });
            }
        }
    }
}