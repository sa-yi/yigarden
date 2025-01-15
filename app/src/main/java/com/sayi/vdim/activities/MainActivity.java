package com.sayi.vdim.activities;

import static com.sayi.vdim.Consts.sp_token;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sayi.MainApplication;
import com.sayi.vdim.Consts;
import com.sayi.vdim.activities.mainfragments.dashboard.DashboardFragment;
import com.sayi.vdim.activities.mainfragments.home.HomeFragment;
import com.sayi.vdim.activities.mainfragments.music.MusicFragment;
import com.sayi.vdim.activities.mainfragments.user.UserFragment;
import com.sayi.vdim.databinding.ActivityMainBinding;
import com.sayi.vdim.utils.DarkModeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static String HOME_FRAGMENT_KEY = "HOME_FRAGMENT_KEY";
    static String DASHBOARD_FRAGMENT_KEY = "DASHBOARD_FRAGMENT_KEY";
    static String MUSIC_FRAGMENT_KEY = "MUSIC_FRAGMENT_KEY";
    static String USER_FRAGMENT_KEY = "USER_FRAGMENT_KEY";
    static String USER_FRAG_KEY = "USER_FRAGM_KEY";

    static String TAG = "MainActivity";
    HomeFragment homeFragment;
    DashboardFragment dashboardFragment;
    MusicFragment musicFragment;
    UserFragment userFragment;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences(Consts.sp_user_data, MODE_PRIVATE);
        //String cookie = pref.getString("cookie", "");
        String token = pref.getString(sp_token, "");
        Log.d(TAG, token);
        /*if (token.isEmpty()) {
            //dialog.show();
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
        recoverInstanceState(savedInstanceState);


        Window window = getWindow();
        /*如果之前是半透明模式，要加这一句需要取消半透明的Flag*/
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

        if (homeFragment == null) homeFragment = new HomeFragment();
        if (dashboardFragment == null) dashboardFragment = new DashboardFragment();
        if (musicFragment == null) musicFragment = new MusicFragment();
        //if (userFragment == null) userFragment = new UserFragment();


        if(userFragment == null) userFragment = new UserFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(dashboardFragment);
        adapter.addFragment(musicFragment);
        adapter.addFragment(userFragment);

        binding.viewpager.setAdapter(adapter);
        binding.navView.setOnItemSelectedListener(item -> {
            int itemCount = binding.navView.getMenu().size();
            for (int i = 0; i < itemCount; i++) {
                if (item == binding.navView.getMenu().getItem(i)) {
                    binding.viewpager.setCurrentItem(i, false);
                    break;
                }
            }
            return true;
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (homeFragment != null) {
            getSupportFragmentManager().putFragment(outState, HOME_FRAGMENT_KEY, homeFragment);
        }
        if (dashboardFragment != null) {
            getSupportFragmentManager().putFragment(outState, DASHBOARD_FRAGMENT_KEY, dashboardFragment);
        }
        if (musicFragment != null) {
            getSupportFragmentManager().putFragment(outState, MUSIC_FRAGMENT_KEY, musicFragment);
        }
        if(userFragment != null) {
            getSupportFragmentManager().putFragment(outState, USER_FRAG_KEY, userFragment);
        }
    }

    private void recoverInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, HOME_FRAGMENT_KEY);
        dashboardFragment = (DashboardFragment) getSupportFragmentManager().getFragment(savedInstanceState, DASHBOARD_FRAGMENT_KEY);
        musicFragment = (MusicFragment) getSupportFragmentManager().getFragment(savedInstanceState, MUSIC_FRAGMENT_KEY);

        userFragment = (UserFragment) getSupportFragmentManager().getFragment(savedInstanceState, USER_FRAG_KEY);
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