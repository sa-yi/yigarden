package com.sayi.yi_garden.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sayi.MainApplication;
import com.sayi.yi_garden.activities.mainfragments.dashboard.DashboardFragment;
import com.sayi.yi_garden.activities.mainfragments.home.HomeFragment;
import com.sayi.yi_garden.activities.mainfragments.music.MusicFragment;
import com.sayi.yi_garden.activities.mainfragments.user.UserFragment;
import com.sayi.yi_garden.databinding.ActivityMainBinding;
import com.sayi.yi_garden.utils.DarkModeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("app基于Cookies运作").setMessage("未获取到Cookies，点击确定键前往获取Cookies，点击取消退出app")
                .setPositiveButton("确定", (dialog, which) -> {
                    startActivity(new Intent(MainActivity.this, LoginWebActivity.class));
                }).setNegativeButton("取消", ((dialog, which) -> {
                    finish();
                }));
        AlertDialog dialog = builder.create();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        //String cookie = pref.getString("cookie", "");
        String cookie = pref.getString("cookie", "wordpress_logged_in_bdf5fb41c0e0e935d2c6e30a9ee42a6c=%E9%A3%92%E7%BF%8A%7C1722172831%7Ce01JqqPzIP2YBmdR84J6C3BZmO8ouqx1zIW0xNKIitF%7C73519d8fef8f27e67fb26cbe3dedcbfa2764ba7afb86f81f875d4cba80504246;");

        if (cookie.isEmpty()) {
            dialog.show();
        }
        MainApplication.cookies = cookie;
        init();
    }

    public void init() {
        Window window = getWindow();
        /*如果之前是办透明模式，要加这一句需要取消半透明的Flag*/
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        binding.viewpager.setOffscreenPageLimit(4);
        binding.viewpager.setPageTransformer((page, position) -> {
            page.setTranslationX(0);
            page.setAlpha(1);
        });
        binding.viewpager.setUserInputEnabled(false);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);

        HomeFragment homeFragment = new HomeFragment();
        DashboardFragment dashboardFragment = new DashboardFragment();
        MusicFragment musicFragment = new MusicFragment();
        UserFragment userFragment = new UserFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(dashboardFragment);
        adapter.addFragment(musicFragment);
        adapter.addFragment(userFragment);

        binding.viewpager.setAdapter(adapter);
        binding.navView.setOnItemSelectedListener(item -> {
            int index = -1;
            int itemCount = binding.navView.getMenu().size();
            for (int i = 0; i < itemCount; i++) {
                if (item == binding.navView.getMenu().getItem(i)) {
                    index = i;
                    break;
                }
            }

            if (index == 3) {
                //startActivity(new Intent(MainActivity.this, LoginWebActivity.class));
            }

            if (index != -1) {
                binding.viewpager.setCurrentItem(index, false);
            }

            return true;
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (DarkModeUtils.isDarkMode(this)) {
            MainApplication.toast("dark mode");
        } else {
            MainApplication.toast("light mode");

        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

}