package com.sayi.yi_garden.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.databinding.UserBannerBinding;

public class UserBanner extends Fragment {
    UserBannerBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=UserBannerBinding.inflate(getLayoutInflater());

        binding.follow.setOnClickListener(v->{});
        binding.chat.setOnClickListener(v->{});

        return binding.getRoot();
    }

    public void setAvator(String url){
        Glide.with(this).load(url).into(binding.avator);
    }
    public void setUserName(String name){
        binding.userName.setText(name);
    }
    public void setSendTime(String time){
        binding.sendTime.setText(time);
    }
}
