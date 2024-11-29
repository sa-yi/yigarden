package com.sayi.vdim.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sayi.vdim.databinding.ActivityNotifyBinding;

public class NotifyActivity extends AppCompatActivity {

    public ActivityNotifyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        binding = ActivityNotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}