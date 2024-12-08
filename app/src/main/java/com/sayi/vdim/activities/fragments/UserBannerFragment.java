package com.sayi.vdim.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sayi.MainApplication;
import com.sayi.vdim.databinding.UserBannerBinding;

public class UserBannerFragment extends Fragment {
    private UserBannerBinding binding;
    private UserBannerViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UserBannerBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(UserBannerViewModel.class);

        // 绑定 ViewModel 数据到 UI
        viewModel.getUserName().observe(getViewLifecycleOwner(), name -> binding.userName.setText(name));
        viewModel.getSendTime().observe(getViewLifecycleOwner(), time -> binding.sendTime.setText(time));

        // 初始化点击事件
        binding.follow.setOnClickListener(v -> MainApplication.toast("followed"));
        binding.chat.setOnClickListener(v -> MainApplication.toast("chatting"));

        binding.getRoot().setOnClickListener(v->{
            MainApplication.toast("userBanner");
        });

        return binding.getRoot();
    }

    public void setAuthorName(String name){
        viewModel.setUserName(name);
    }
    public void setSendTime(String time){
        viewModel.setSendTime(time);
    }

    public ImageView getAvatorView(){
        return binding.avator;
    }
}
