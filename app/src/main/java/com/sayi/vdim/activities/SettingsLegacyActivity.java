package com.sayi.vdim.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.sayi.MainApplication;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.ActivitySettingsBinding;
import com.sayi.vdim.utils.DarkModeUtils;

import java.util.Objects;

public class SettingsLegacyActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("theme", false);
        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
            switch (Objects.requireNonNull(key)) {
                case "theme":
                    boolean ifDark = sharedPreferences1.getBoolean("theme", false);
                    if (ifDark) {
                        DarkModeUtils.applyNightMode(SettingsLegacyActivity.this);
                    } else {
                        DarkModeUtils.applyDayMode(SettingsLegacyActivity.this);
                    }
                    break;
                case "use_network":
                    boolean useNetwork = sharedPreferences1.getBoolean("use_network", false);
                    if (useNetwork) {
                        MainApplication.toast("开启在线歌单，重启后生效");
                    } else {
                        MainApplication.toast("使用本地歌单，重启后生效");
                    }
                    break;
                default:
                    MainApplication.toast(key);
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