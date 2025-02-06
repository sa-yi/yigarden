package com.sayi.vdim.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sayi.vdim.adapter.MemberAdapter;
import com.sayi.vdim.adapter.MemberClassAdapter;
import com.sayi.vdim.databinding.ActivityFollowingListBinding;

public class FollowingListActivity extends AppCompatActivity {
    ActivityFollowingListBinding binding;
    MemberClassAdapter memberClassAdapter;
    MemberAdapter memberAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFollowingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(v-> finish());

        memberClassAdapter=new MemberClassAdapter();
        binding.classification.setAdapter(memberClassAdapter);
        binding.classification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        memberAdapter=new MemberAdapter();
        binding.followersList.setAdapter(memberAdapter);
        binding.followersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
