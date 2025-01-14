package com.sayi.vdim.activities;

import android.graphics.*;
import android.os.*;
import android.view.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.fragment.app.*;
import androidx.viewpager2.adapter.*;

import com.google.android.material.tabs.*;
import com.sayi.vdim.activities.fragments.*;
import com.sayi.vdim.databinding.*;


public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);
        window.setStatusBarColor(Color.TRANSPARENT);

        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.gradientTextView.setColor(Color.rgb(255, 0, 0), Color.rgb(0, 0, 255));


        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();

        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 1) return registerFragment;
                return loginFragment;
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
        binding.loginFragment.setUserInputEnabled(false);
        binding.loginFragment.setAdapter(adapter);

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.loginFragment.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
