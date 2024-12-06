package com.sayi.vdim.activities;

import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.sayi.*;
import com.sayi.vdim.databinding.ActivityForumBinding;
import com.sayi.vdim.dz_entity.*;

import retrofit2.*;

public class ForumActivity extends AppCompatActivity {
    ActivityForumBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        int fid=intent.getIntExtra("fid",-1);
        MainApplication.toast(fid+"");

        binding.textView.setText("");

        DzService dzService=DzClient.getRetrofitInstance().create(DzService.class);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
